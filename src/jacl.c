/*
 * Copyright (C) 2003-2006 Nick Shrine <N.R.Shrine@cs.bham.ac.uk>
 *
 * This file is part of Jfacl.
 *
 * Jfacl is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Jfacl is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Jfacl; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */

#include <jni.h>
#include "ufsaclwin.h"
#include <sys/acl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <pwd.h>
#include <grp.h>
#include <alloca.h>
#include <errno.h>
#include <string.h>
#include <strings.h>
#include <stdio.h>
#include <stdlib.h>
#include "getname.h"

#ifdef LINUX
#include "linux/uacl.h"
#define acl(A, B, C, D) _acl(A, B, C, D)
#define USER ACL_USER
#define USER_OBJ ACL_USER_OBJ
#define GROUP ACL_GROUP
#define GROUP_OBJ ACL_GROUP_OBJ
#endif

extern int  snprintf(char *, size_t, const char *, ...);

JNIEXPORT jobject JNICALL Java_org_flimbar_security_acl_UfsAcl_getacl(
		JNIEnv *env, jclass obj, jstring path)
{
    int i, length;
    const char* pathp;
    char *msg;
    char *entry_name = NULL;
    aclent_t aclpbuf[MAX_ACL_ENTRIES];
    jobject aclentry;
    jobject aclentries;
    jint n;
    jclass ArrayListCls, AclExCls, AclEntCls;
    jmethodID ArrayListCid, ArrayListAid, AclEntCid;
    
    ArrayListCls = (*env)->FindClass(env, "java/util/ArrayList");
    ArrayListCid = (*env)->GetMethodID(env, ArrayListCls, "<init>", "()V");
    ArrayListAid = (*env)->GetMethodID(env, ArrayListCls, "add", "(Ljava/lang/Object;)Z");
    AclExCls = (*env)->FindClass(env, "org/flimbar/security/acl/UfsAclException");
    AclEntCls = (*env)->FindClass(env, "org/flimbar/security/acl/UfsAclEntry");
    AclEntCid = (*env)->GetMethodID(env, AclEntCls, "<init>", "(IILjava/lang/String;I)V");

    pathp = (*env)->GetStringUTFChars(env, path, 0);
    n = acl(pathp, GETACL, MAX_ACL_ENTRIES, aclpbuf);
    (*env)->ReleaseStringUTFChars(env, path, pathp);
    /* If there was an error throw an exception */
    if (n == -1) {
        length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char) ;
        msg = alloca(length); /* I have no clue what happens at (*env)->ThrowNew(env, AclExCls, msg); */
        if (msg) {
            bzero(msg, length);
            snprintf(msg, length, "ACL: [%d] %s", errno, strerror(errno));
            (*env)->ThrowNew(env, AclExCls, msg);
        }
        return NULL; /* XXX */
    }
    
    /* Create a new Object array containing n AclEnts */
    aclentries = (*env)->NewObject(env, ArrayListCls, ArrayListCid);
    for (i = 0; i < n; i++) {
        entry_name = NULL;
        /* Time to figure out if aclpbuf[i] is a groups or a user */
        if (aclpbuf[i].a_type & (USER_OBJ | USER)) {
            struct passwd *pwent = getpwuid(aclpbuf[i].a_id);
            if (pwent != NULL)
                entry_name = getNameFromPwent(pwent);
        } else if (aclpbuf[i].a_type & (GROUP_OBJ | GROUP)) {
            struct group *grent = getgrgid(aclpbuf[i].a_id);
            if (grent != NULL)
                entry_name = getGroupFromGrent(grent);
        }

        if (entry_name == NULL) {
            entry_name = malloc(256);
            bzero(entry_name, 256);
            snprintf(entry_name, 256, "%d", aclpbuf[i].a_id);
        }
        aclentry = (*env)->NewObject(env, AclEntCls, AclEntCid, aclpbuf[i].a_type, aclpbuf[i].a_id,
				(*env)->NewStringUTF(env, entry_name), aclpbuf[i].a_perm);
        (*env)->CallVoidMethod(env, aclentries, ArrayListAid, aclentry);
        free(entry_name); 
    }
    return aclentries;
}

JNIEXPORT jint JNICALL Java_org_flimbar_security_acl_UfsAcl_setacl(JNIEnv *env, jclass obj, jstring path, jobject aclentries, jint size)
{
    int i, which, length;
    const char *pathp;
    char *msg;
    jobject entry;
    aclent_t aclpbuf[size];
    jint n;
    jclass ArrayListCls, AclExCls, AclEntCls;
    jmethodID ArrayListGid, AclEntTid, AclEntNid, AclEntPid;
    
    ArrayListCls = (*env)->FindClass(env, "java/util/ArrayList");
    ArrayListGid = (*env)->GetMethodID(env, ArrayListCls, "get", "(I)Ljava/lang/Object;");
    AclExCls = (*env)->FindClass(env, "org/flimbar/security/acl/UfsAclException");
    AclEntCls = (*env)->FindClass(env, "org/flimbar/security/acl/UfsAclEntry");
    AclEntTid = (*env)->GetMethodID(env, AclEntCls, "getType", "()I");
    AclEntNid = (*env)->GetMethodID(env, AclEntCls, "getId", "()I");
    AclEntPid = (*env)->GetMethodID(env, AclEntCls, "getMode", "()I");
    
    pathp = (*env)->GetStringUTFChars(env, path, 0);
    
    for (i = 0; i < size; i++) {
        entry = (*env)->CallObjectMethod(env, aclentries, ArrayListGid, i);
        aclpbuf[i].a_type = (*env)->CallIntMethod(env, entry, AclEntTid);
        aclpbuf[i].a_id = (*env)->CallIntMethod(env, entry, AclEntNid);
        aclpbuf[i].a_perm = (*env)->CallIntMethod(env, entry, AclEntPid);
    }
    
#ifndef LINUX
    n = aclcheck(aclpbuf, size, &which);
    if (n != 0) {
        length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char) ;
        msg = alloca(length);
        if (msg) {
            bzero(msg, length);
            snprintf(msg, length, "CHK: [%d] %s", errno, strerror(errno));
            (*env)->ThrowNew(env, AclExCls, msg);
        }
        return n;
    }
#endif
	
    n = acl(pathp, SETACL, size, aclpbuf);
	(*env)->ReleaseStringUTFChars(env, path, pathp);
	if (n == -1) {
#ifdef LINUX
		if (errno == 95) {
			(*env)->ThrowNew(env, AclExCls, "ACL95");
			return -1;
		}
#endif
		length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char);
        msg = alloca(length);
        if (msg) {
            bzero(msg, length);
            snprintf(msg, length, "ACL: [%d] %s", errno, strerror(errno));
            (*env)->ThrowNew(env, AclExCls, msg);
        }
        return -1;
    }
	
    return n;
}
