/*
   Most code copied from getfacl.c in acl-2.2.23
 */

#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/acl.h>
#include <acl/libacl.h>
#include <stdlib.h>
#include <errno.h>
#include "uacl.h"
		
static int getmode(acl_permset_t permset)
{
	int mode = 0;
	
	if (acl_get_perm(permset, ACL_READ)) {
		mode |= ACL_READ;
	}
	
	if (acl_get_perm(permset, ACL_WRITE)) {
		mode |= ACL_WRITE;
	}
	
	if (acl_get_perm(permset, ACL_EXECUTE)) {
		mode |= ACL_EXECUTE;
	}
	
	return mode;
}

static void setmode(acl_permset_t *permset_p, int mode)
{
	if (mode & ACL_READ) {
		acl_add_perm(*permset_p, ACL_READ);
	}

	if (mode & ACL_WRITE) {
		acl_add_perm(*permset_p, ACL_WRITE);
	}
	
	if (mode & ACL_EXECUTE) {
		acl_add_perm(*permset_p, ACL_EXECUTE);
	}
}

aclent_t getentry(acl_entry_t acl_entry, struct stat st, int acl_default) 
{	
	aclent_t aclent;
	
	acl_tag_t e_type;
	acl_get_tag_type(acl_entry, &e_type);	
	acl_permset_t permset;
	acl_get_permset(acl_entry, &permset);
	void *qualifier = acl_get_qualifier(acl_entry);
	
	aclent.a_perm = getmode(permset);
	
	switch(e_type) {
		case ACL_USER_OBJ:
			aclent.a_id = st.st_uid;
			break;
			
		case ACL_USER:
			aclent.a_id = *(uid_t*)qualifier;			
			break;
			
		case ACL_GROUP_OBJ:
			aclent.a_id = st.st_gid;
			break;
			
		case ACL_GROUP:
			aclent.a_id = *(gid_t*)qualifier;
			break;
			
		case ACL_MASK:
			aclent.a_id = -1;
			break;
			
		case ACL_OTHER:
			aclent.a_id = -1;
			break;
	}
	
	aclent.a_type = e_type | acl_default;
	
	if(qualifier != NULL) {
		acl_free(qualifier);
	}
	
	return aclent;
}

int getacl(const char *pathp, aclent_t *aclpbuf)
{
	acl_t acl = NULL, default_acl = NULL;	
	struct stat st;
	
	if (stat(pathp, &st) != 0) {
		return -1;
	}
		
	acl = acl_get_file(pathp, ACL_TYPE_ACCESS);
	if(acl == NULL && (errno == ENOSYS || errno == ENOTSUP)) {
		acl = acl_from_mode(st.st_mode);
		if (acl == NULL) {
			return -1;
		}
	}	
	
	if (S_ISDIR(st.st_mode)) {
		default_acl = acl_get_file(pathp, ACL_TYPE_DEFAULT);
		if ((default_acl != NULL) && (acl_entries(default_acl) == 0)) {
			acl_free(default_acl);
			default_acl = NULL;
		} 
	}
	
	acl_entry_t acl_entry;
	int ret = acl_get_entry(acl, ACL_FIRST_ENTRY, &acl_entry);
	int i = 0;
	while(ret > 0) {
		aclpbuf[i++] = getentry(acl_entry, st, 0);
		ret = acl_get_entry(acl, ACL_NEXT_ENTRY, &acl_entry);
	}
	acl_free(acl);
	
	if((default_acl != NULL) && (ret != -1)) {
		ret = acl_get_entry(default_acl, ACL_FIRST_ENTRY, &acl_entry);
		while(ret > 0) {			
			aclpbuf[i++] = getentry(acl_entry, st, ACL_DEFAULT);
			ret = acl_get_entry(default_acl, ACL_NEXT_ENTRY, &acl_entry);
		}
		acl_free(default_acl);
	}
	
	return i;
}

int setacl(const char *pathp, int size, aclent_t *aclpbuf)
{
	acl_t acl = NULL, default_acl = NULL, *current;
	acl_entry_t entry;
	acl_permset_t permset;
	int i, result, default_count = 0;
	
	for (i = 0; i < size; i++) {
		if (aclpbuf[i].a_type & ACL_DEFAULT) {
			default_count++;
		}
	}	
	acl = acl_init(size - default_count);
	if (default_count > 0) {
		default_acl = acl_init(default_count);
	}
	
	for (i = 0; i < size; i++) {		
		if (aclpbuf[i].a_type & ACL_DEFAULT) {						
			current = &default_acl;			
		} else {
			current = &acl;			
		}		
		acl_create_entry(current, &entry);		
		acl_get_permset(entry, &permset);		
		setmode(&permset, aclpbuf[i].a_perm);		
		acl_set_tag_type(entry, aclpbuf[i].a_type & ~ACL_DEFAULT);
		acl_set_qualifier(entry, &aclpbuf[i].a_id);
		acl_free(entry);
	}
	
	result = acl_set_file(pathp, ACL_TYPE_ACCESS, acl);
	if (result != 0 && (errno == ENOSYS || errno == ENOTSUP)) {		
        mode_t mode;
                                                                                                                                                             
        if (acl_equiv_mode(acl, &mode) == 0) {			
			result = chmod(pathp, mode);			
		}
	} 
	acl_free(acl);
	if (result == 0) {
		if (default_acl == NULL) {
			result = acl_delete_def_file(pathp);
		} else {
			result = acl_set_file(pathp, ACL_TYPE_DEFAULT, default_acl);
			acl_free(default_acl);
		}
	}
	
	return result;
}

int _acl(const char* pathp, int op, int size, aclent_t* aclpbuf)
{
	if (op == GETACL) {
		return getacl(pathp, aclpbuf);
	} else if (op == SETACL) {
		return setacl(pathp, size, aclpbuf);
	}
}
