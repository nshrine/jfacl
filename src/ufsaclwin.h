#include <jni.h>

#ifndef _Included_org_flimbar_security_acl_UfsAclEntry
#define _Included_org_flimbar_security_acl_UfsAclEntry
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     org_flimbar_security_acl_UfsAcl
 * Method:    getacl
 * Signature: (Ljava/lang/String;)Ljava/util/Vector;
 */
JNIEXPORT jobject JNICALL Java_org_flimbar_security_acl_UfsAcl_getacl
  (JNIEnv *, jclass, jstring);

/*
 * Class:     org_flimbar_security_acl_UfsAcl
 * Method:    setacl
 * Signature: (Ljava/lang/String;Ljava/util/Vector;)I
 */
JNIEXPORT jint JNICALL Java_org_flimbar_security_acl_UfsAcl_setacl
  (JNIEnv *, jclass, jstring, jobject, jint);

/*
 * Class:     org_flimbar_security_acl_AddDialog
 * Method:    getall
 * Signature: ()Ljava/util/Vector;
 */
JNIEXPORT jobject JNICALL Java_org_flimbar_security_acl_UfsAcl_getall
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
