#include <jni.h>
#include "uk_ac_bham_cs_security_auth_module_Posix.h"
#include <pwd.h>

jobject get_jobject(JNIEnv *env, struct passwd *pwent)
{
    jclass UnixUser;
    jmethodID UnixUserCid;
    jobject obj;
    jstring name, password, gecos, dir, shell;
    jlong uid, gid;

    UnixUser = (*env)->FindClass(env, "uk/ac/bham/cs/security/auth/UnixUser");
    UnixUserCid = (*env)->GetMethodID(env, UnixUser, "<init>",
            "(Ljava/lang/String;Ljava/lang/String;JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
    
    name = (*env)->NewStringUTF(env, pwent->pw_name);
    password = (*env)->NewStringUTF(env, pwent->pw_passwd),
    uid = pwent->pw_uid;
    gid = pwent->pw_gid;
    gecos = (*env)->NewStringUTF(env, pwent->pw_gecos);
    dir = (*env)->NewStringUTF(env, pwent->pw_dir);
    shell = (*env)->NewStringUTF(env, pwent->pw_shell);
    
    obj = (*env)->NewObject(env, UnixUser, UnixUserCid, name, password,
            uid, gid, gecos, dir, shell);
    
    return obj;
}

JNIEXPORT jobject JNICALL Java_uk_ac_bham_cs_security_auth_module_Posix_getUserByName
        (JNIEnv *env, jclass obj, jstring jname) 
{
    struct passwd *pwent;
    const char *name;
    jobject result;

    name = (*env)->GetStringUTFChars(env, jname, NULL);
    pwent = getpwnam(name);
    (*env)->ReleaseStringUTFChars(env, jname, name);    

    if (pwent != NULL) {
        result = get_jobject(env, pwent);
    }
    
    return result;
}

JNIEXPORT jobject JNICALL Java_uk_ac_bham_cs_security_auth_module_Posix_getUserByUid
  (JNIEnv *env, jclass obj, jlong juid)
{
    struct passwd *pwent;
    jobject result;
    
    pwent = getpwuid(juid);

    if (pwent != NULL) {
        result = get_jobject(env, pwent);
    }
    
    return result;
}


