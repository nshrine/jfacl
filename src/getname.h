#ifndef __GETNAME_H_
#define __GETNAME_H_

#include <pwd.h>

char* getNameFromPwent(struct passwd *pwent);
char* getGroupFromGrent(struct group *grent);

#endif
