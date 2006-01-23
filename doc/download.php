<?php
	ini_set("SMTP", "mail-relay");
	mail("nrs@cs.bham.ac.uk", "Jfacl download", $_SERVER['REMOTE_ADDR'], "From: nrs@cs.bham.ac.uk\r\n");
	header("Location: http://www.cs.bham.ac.uk/~nrs/jfacl/jfacl-0.4.2.tar.gz");
?>
