<?php
	session_start();
	if(empty($_SESSION['mdp']))
	{
		include('menu.php');
	}
	else
	{
		include('menuc.php');
	}
?>

<html>
	<body>
		<div class="main">
			[VIDEO DE PRESENTATION]
		</div>
	</body>
</html>