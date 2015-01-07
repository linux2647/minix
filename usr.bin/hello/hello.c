/*	$NetBSD: hello.c,v 1.12 2014/01/05 13:29:25 showell Exp $	*/

#include <sys/cdefs.h>

#ifndef lint
#if 0
static char sccsid[] = "@(#)dirname.c	8.4 (Berkeley) 5/4/95";
#endif
__RCSID("$NetBSD: hello.c,v 1.12 2014/01/05 13:29:25 showell Exp $");
#endif /* not lint */

#include <stdio.h>
#include <stdlib.h>

#define S(x) #x
#define STR(X) S(X)

int
main(int argc, char **argv)
{
  int i = 0;

  if (argc < 2)
  {
#ifdef NAME
    (void)printf("Hello %s!\n", STR(NAME));
#else
    (void)printf("Hello!\n");
#endif
  }
  else
  {
    for (i = 0; i < argc - 1; ++i)
    {
      (void)printf("Hello %s!\n", argv[i + 1]);
    }
  }
  exit(0);
}
