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
#include <pwd.h>
#include <grp.h>
#include <errno.h>
#include "getname.h"

#ifdef LINUX
#define USER ACL_USER
#define GROUP ACL_GROUP
#endif

#define DEF_MODE 5

JNIEXPORT jobject JNICALL Java_uk_ac_bham_cs_security_acl_UfsAcl_getall(JNIEnv *env, jclass obj)
{    
    struct passwd *pwent;
    struct group *grent;
    char *entry_name;
    
    jclass TreeSetCls, AclEntCls, AclCls;
    jmethodID TreeSetCid, TreeSetAid, TreeSetSid, AclEntCid, SetCountid;
    jobject entries, entry;
    jint namecount;
    
    TreeSetCls = (*env)->FindClass(env, "java/util/TreeSet");
    TreeSetCid = (*env)->GetMethodID(env, TreeSetCls, "<init>", "()V");
    TreeSetAid = (*env)->GetMethodID(env, TreeSetCls, "add", "(Ljava/lang/Object;)Z");
    TreeSetSid = (*env)->GetMethodID(env, TreeSetCls, "size", "()I");    
    AclEntCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAclEntry");
    AclEntCid = (*env)->GetMethodID(env, AclEntCls, "<init>", "(IILjava/lang/String;I)V");
    AclCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAcl");
    SetCountid = (*env)->GetStaticMethodID(env, AclCls, "setNameCount", "(I)V");    

    entries = (*env)->NewObject(env, TreeSetCls, TreeSetCid);
    
    setpwent();
    while((pwent = getpwent()) != NULL) {
        entry_name = getNameFromPwent(pwent);
        entry = (*env)->NewObject(env, AclEntCls, AclEntCid, USER, pwent->pw_uid, (*env)->NewStringUTF(env, entry_name), DEF_MODE);
        (*env)->CallVoidMethod(env, entries, TreeSetAid, entry);
        namecount = (*env)->CallIntMethod(env, entries, TreeSetSid);
        (*env)->CallStaticVoidMethod(env, AclCls, SetCountid, namecount);
    }
    endpwent();

    setgrent();
    while((grent = getgrent()) != NULL) {
        entry_name = getGroupFromGrent(grent);
        entry = (*env)->NewObject(env, AclEntCls, AclEntCid, GROUP, grent->gr_gid, (*env)->NewStringUTF(env, entry_name), DEF_MODE);
        (*env)->CallVoidMethod(env, entries, TreeSetAid, entry);
        namecount = (*env)->CallIntMethod(env, entries, TreeSetSid);
        (*env)->CallStaticVoidMethod(env, AclCls, SetCountid, namecount);
    }
    endgrent();
    (*env)->CallStaticVoidMethod(env, AclCls, SetCountid, namecount);

    return entries;
}
