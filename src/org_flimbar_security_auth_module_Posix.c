#include <jni.h>
#include "org_flimbar_security_auth_module_Posix.h"
#include <pwd.h>
#include <grp.h>

jobject get_unix_user(JNIEnv *, struct passwd *);
jobject get_unix_group(JNIEnv *, struct group *);

JNIEXPORT jobject JNICALL Java_org_flimbar_security_auth_module_Posix_getUserByName
        (JNIEnv *env, jclass obj, jstring jname) 
{
    struct passwd *pwent;
    const char *name;
    jobject result;

    name = (*env)->GetStringUTFChars(env, jname, NULL);
    pwent = getpwnam(name);
    (*env)->ReleaseStringUTFChars(env, jname, name);    

    if (pwent != NULL) {
        result = get_unix_user(env, pwent);
    }
    
    return result;
}

JNIEXPORT jobject JNICALL Java_org_flimbar_security_auth_module_Posix_getUserByUid
  (JNIEnv *env, jclass obj, jlong juid)
{
    struct passwd *pwent;
    jobject result;
    
    pwent = getpwuid(juid);

    if (pwent != NULL) {
        result = get_unix_user(env, pwent);
    }
    
    return result;
}

JNIEXPORT jobject JNICALL Java_org_flimbar_security_auth_module_Posix_getGroupByName
  (JNIEnv *env, jobject obj, jstring jname)
{
    struct group *grent;
    const char *name;
    jobject result;

    name = (*env)->GetStringUTFChars(env, jname, NULL);
    grent = getgrnam(name);
    (*env)->ReleaseStringUTFChars(env, jname, name);    

    if (grent != NULL) {
        result = get_unix_group(env, grent);
    }

    return result;
}

JNIEXPORT jobject JNICALL Java_org_flimbar_security_auth_module_Posix_getGroupByGid
  (JNIEnv *env, jobject obj, jlong jgid)
{
    struct group *grent;    
    jobject result;
    
    grent = getgrgid(jgid);

    if (grent != NULL) {
        result = get_unix_group(env, grent);
    }

    return result;
}

jobject get_unix_user(JNIEnv *env, struct passwd *pwent)
{
    jclass UnixUser;
    jmethodID UnixUserCid;
    jobject obj;
    jstring name, password, gecos, dir, shell;
    jlong uid, gid;

    UnixUser = (*env)->FindClass(env, "org/flimbar/security/auth/UnixUser");
    UnixUserCid = (*env)->GetMethodID(env, UnixUser, "<init>",
            "(Ljava/lang/String;Ljava/lang/String;JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    
    name = (*env)->NewStringUTF(env, pwent->pw_name);
    password = (*env)->NewStringUTF(env, pwent->pw_passwd),
    uid = pwent->pw_uid;
    gid = pwent->pw_gid;
    gecos = (*env)->NewStringUTF(env, pwent->pw_gecos);
    dir = (*env)->NewStringUTF(env, pwent->pw_dir);
    shell = (*env)->NewStringUTF(env, pwent->pw_shell);
    
    obj = (*env)->NewObject(env, UnixUser, UnixUserCid,
            name, password, uid, gid, gecos, dir, shell);
    
    return obj;
}

jobject get_unix_group(JNIEnv *env, struct group *grent)
{
    jclass UnixGroup, String;
    jmethodID UnixGroupCid;
    jobject obj;
    jstring name, password, member;
    jobjectArray members;
    int i, nmem;
    jlong gid;

    UnixGroup = (*env)->FindClass(env, "org/flimbar/security/auth/UnixGroup");
    String = (*env)->FindClass(env, "java/lang/String");
    UnixGroupCid = (*env)->GetMethodID(env, UnixGroup, "<init>",
            "(Ljava/lang/String;Ljava/lang/String;J[Ljava/lang/String;)V");

    name = (*env)->NewStringUTF(env, grent->gr_name);
    password = (*env)->NewStringUTF(env, grent->gr_passwd);
    gid = grent->gr_gid;
    
    nmem = 0;
    while (grent->gr_mem[nmem] != NULL) {
        nmem++;
    }    
    members = (*env)->NewObjectArray(env, nmem, String, NULL);   
    for (i = 0; i < nmem; i++) {     
        member = (*env)->NewStringUTF(env, grent->gr_mem[i]);
        (*env)->SetObjectArrayElement(env, members, i, member);
    }
    
    obj = (*env)->NewObject(env, UnixGroup, UnixGroupCid,
            name, password, gid, members);

    return obj;
}
