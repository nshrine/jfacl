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
#include <string.h>
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
