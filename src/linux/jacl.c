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
#include <strings.h>
#include <stdio.h>
#include <stdlib.h>
#include "getname.h"

#ifdef LINUX
#define aclent_t acl_entry_t
#define USER ACL_USER
#define GROUP ACL_GROUP
#define USER_OBJ ACL_USER_OBJ
#define GROUP_OBJ ACL_GROUP_OBJ
#endif

extern int  snprintf(char *, size_t, const char *, ...);

JNIEXPORT jobject JNICALL Java_uk_ac_bham_cs_security_acl_UfsAcl_getacl(JNIEnv *env, jclass obj, jstring path)
{    
    int i, length;
    const char *pathp;
    char *msg;
    char *entry_name = NULL;
    acl_t acl;
    jobject aclentry;
    jobject aclentries;
    jint n;
    
    jclass ArrayListCls = (*env)->FindClass(env, "java/util/ArrayList");
    jmethodID ArrayListCid = (*env)->GetMethodID(env, ArrayListCls, "<init>", "()V");
    jmethodID ArrayListAid = (*env)->GetMethodID(env, ArrayListCls, "add", "(Ljava/lang/Object;)Z");
    jclass AclExCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAclException");
    jclass AclEntCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAclEntry");
    jmethodID AclEntCid = (*env)->GetMethodID(env, AclEntCls, "<init>", "(IILjava/lang/String;I)V");

    const char *pathp = (*env)->GetStringUTFChars(env, path, 0);
    
    int entry_id[2];
    entry_id[0] = ACL_TYPE_ACCESS;
    entry_id[1] = ACL_TYPE_DEFAULT;
    
    int e;
    for(e = 0; e < 2; e++) {
	    acl = acl_get_file(pathp, entry_id[e]);
	    
	    /* If there was an error throw an exception */
	    if(acl == NULL) {
		    length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char) ;
		    char *msg = alloca(length); 
		    if(msg) {
				bzero(msg, length);
				snprintf(msg, length, "ACL: [%d] %s", errno, strerror(errno));
				/* Fahlgren: I have no clue what happens at (*env)->ThrowNew(env, AclExCls, msg); */
				(*env)->ThrowNew(env, AclExCls, msg);
			}
			return NULL; /* XXX */
	    }
		n = acl_entries(acl);

		/* Create a new Object array containing n AclEnts */
		aclentries = (*env)->NewObject(env, ArrayListCls, ArrayListCid);
		for(i=0; i<n; i++) {
			entry_name = NULL;
        /* Time to figure out if aclpbuf[i] is a groups or a user */
	acl_tag_type *type;
	acl_get_tag_type(acl, type);
	if(&type & (USER_OBJ | USER)) {
            struct passwd *pwent = getpwuid(
        if(aclpbuf[i].a_type & (USER_OBJ | USER)) {
            struct passwd *pwent = getpwuid(aclpbuf[i].a_id);
            if(pwent != NULL)
                entry_name = getNameFromPwent(pwent);
        } else if(aclpbuf[i].a_type & (GROUP_OBJ | GROUP)) {
            struct group *grent = getgrgid(aclpbuf[i].a_id);
            if(grent != NULL)
                entry_name = getGroupFromGrent(grent);
        }

        if(entry_name == NULL) {
            entry_name = malloc(256);
            bzero(entry_name, 256);
            snprintf(entry_name, 256, "%d", aclpbuf[i].a_id);
        }
        aclentry = (*env)->NewObject(env, AclEntCls, AclEntCid, aclpbuf[i].a_type, aclpbuf[i].a_id, (*env)->NewStringUTF(env, entry_name), aclpbuf[i].a_perm);
        (*env)->CallVoidMethod(env, aclentries, ArrayListAid, aclentry);
	(*env)->ReleaseStringUTFChars(env, path, pathp);    
        free(entry_name); 
    }
    return aclentries;
}

JNIEXPORT jint JNICALL Java_uk_ac_bham_cs_security_acl_UfsAcl_setacl(JNIEnv *env, jclass obj, jstring path, jobject aclentries, jint size)
{
    int i, which, length;
    const char *pathp;
    char *msg;
    jobject entry;
    aclent_t aclpbuf[MAX_ACL_ENTRIES];
    jint n;
    jclass ArrayListCls, AclExCls, AclEntCls;
    jmethodID ArrayListGid, AclEntTid, AclEntNid, AclEntPid;
    
    ArrayListCls = (*env)->FindClass(env, "java/util/ArrayList");
    ArrayListGid = (*env)->GetMethodID(env, ArrayListCls, "get", "(I)Ljava/lang/Object;");
    AclExCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAclException");
    AclEntCls = (*env)->FindClass(env, "uk/ac/bham/cs/security/acl/UfsAclEntry");
    AclEntTid = (*env)->GetMethodID(env, AclEntCls, "getType", "()I");
    AclEntNid = (*env)->GetMethodID(env, AclEntCls, "getId", "()I");
    AclEntPid = (*env)->GetMethodID(env, AclEntCls, "getMode", "()I");
    
    pathp = (*env)->GetStringUTFChars(env, path, 0);
    
    for(i=0; i<size; i++) {
        entry = (*env)->CallObjectMethod(env, aclentries, ArrayListGid, i);
        aclpbuf[i].a_type = (*env)->CallIntMethod(env, entry, AclEntTid);
        aclpbuf[i].a_id = (*env)->CallIntMethod(env, entry, AclEntNid);
        aclpbuf[i].a_perm = (*env)->CallIntMethod(env, entry, AclEntPid);
    }
    
    n = aclcheck(aclpbuf, size, &which);
    if(n != 0) {
        length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char) ;
        msg = alloca(length);
        if(msg) {
            bzero(msg, length);
            snprintf(msg, length, "CHK: [%d] %s", errno, strerror(errno));
            (*env)->ThrowNew(env, AclExCls, msg);
        }
        return n;
    }
    n = acl(pathp, SETACL, size, aclpbuf);
    (*env)->ReleaseStringUTFChars(env, path, pathp);
    if(n == -1) {
        length = sizeof(char) * strlen(strerror(errno)) + 13 * sizeof(char) ;
        msg = alloca(length);
        if(msg) {
            bzero(msg, length);
            snprintf(msg, length, "ACL: [%d] %s", errno, strerror(errno));
            (*env)->ThrowNew(env, AclExCls, msg);
        }
        return -1; /* XXX Better than just a plain return... */
    }
    return n;
}
