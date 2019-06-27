<?php
	session_start();
	$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
	$msgCount=$db->prepare('SELECT ID FROM messages WHERE dest = ? AND arch = ?');
	$msgCount->execute(array($_SESSION['id'], 0));
	$count=$msgCount->rowCount();
	echo "Mon compte (".$count.")";
?>