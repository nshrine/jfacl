#include <jni.h>
#include "ufsaclwin.h"
#include <sys/acl.h>
#include <pwd.h>
#include <grp.h>
#include <errno.h>
#include <strings.h>
#include <stdlib.h>

extern int  snprintf(char *, size_t, const char *, ...);

char* getNameFromPwent(struct passwd *pwent)
{
    char *delim = NULL;
    char *tmpname;
   
    tmpname = malloc(255 * sizeof(char));
    bzero(tmpname, 255 * sizeof(char));
    if(pwent->pw_gecos && strlen(pwent->pw_gecos) && pwent->pw_gecos[0] != ',') {
        if((delim = strchr(pwent->pw_gecos, ',')) != NULL)
            *delim = '\0';
        snprintf(tmpname, 255, "[%s] %s", pwent->pw_name,  pwent->pw_gecos);
    } else {
        snprintf(tmpname, 255, "[%s]", pwent->pw_name);
    }

    return tmpname;
}

char* getGroupFromGrent(struct group *grent)
{
    char *tmpname;
   
    tmpname = malloc(255 * sizeof(char));
    bzero(tmpname, 255 * sizeof(char));
    if(grent->gr_name && strlen(grent->gr_name)) {
        snprintf(tmpname, 255, "%s", grent->gr_name);
    } else {
        snprintf(tmpname, 255, "%d", grent->gr_gid);
    }

   return tmpname;
}
