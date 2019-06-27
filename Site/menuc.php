<?php
	$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
	$msg = "";
	
	if(isset($_POST['unconn']))
	{
		$setconn = $db->prepare('UPDATE accounts SET conn = ? WHERE mdp = ?');
		$setconn->execute(array(0, $_SESSION['mdp']));
		session_unset();
		session_destroy();
		session_write_close();
		setcookie(session_name(),'',0,'/');
		header('Location: accueil.php');
	}
	
	if(($_SESSION['mdp'] != ""))
	{
?>
		<html>
			<head>
				<title>Pan Pan Boum Boum</title>
				<link rel="stylesheet" href="style.css"/>
			</head>
			<body>
				<ul>
					<li><a href="accueil.php">Accueil</a></li>
					<li><a id="count" href="compte.php">Mon compte (0)</a></li>
					<li><a href="jeu.zip" download>Télécharger</a></li>
				</ul>
				<div class="conn">
					<form method="POST" action="">
						<table class="conn" width="100%">
							<tr align="center">
								<td>
									</br></br>
									Bienvenue, <?php echo $_SESSION['id']; ?>
								</td>
							</tr>
							<tr align="center">
								<td>
									</br>
									<input type="submit" name="unconn" value="déconnexion"></input>
								</td>
							</tr>
						</table>
					</form>
				</div>
				<img class="shooter" name='shooter' src="layinShooterOff.png" width="10%" onmouseover="shooter.src='layinShooterOn.png'" onmouseout="shooter.src='layinShooterOff'"/>
			</body>
			<footer  onmouseover="shooter.src='layinShooterOn.png'" onmouseout="shooter.src='layinShooterOff'">
				<table width="5%">
					<tr align='center'>
						<td><a target="_blank" rel="noopener noreferrer" href="https://github.com/Delectus98/Tactical"><img src="git.png" width='70%'></img></a></td>
						<td><a target="_blank" rel="noopener noreferrer" href="https://github.com/Delectus98/Tactical">Github</a></td>
					</tr>
				</table>
			</footer>
		</html>
		<script src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript">
			$(document).ready(function()
			{
				setInterval(function()
				{
					$("#count").load("msgnb.php");
				}, 5000);
			});

		</script>
<?php
		include('conn.php');
	}
	else
	{
		include('menu.php');
	}
?>