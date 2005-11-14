#include <sys/types.h>
#include <sys/stat.h>

#define MAX_ACL_ENTRIES 1024
#define GETACL 0
#define SETACL 1

typedef struct acl {
	int		a_type;		/* the type of ACL entry */
	uid_t	a_id;		/* the entry in -uid or gid */
	mode_t	a_perm;		/* the permission field */
} aclent_t;

int _acl(const char*, int op, aclent_t*);
