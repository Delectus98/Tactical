<?php
	$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
	$msg = "";
	if(isset($_POST['conn']))
	{
		$id = htmlspecialchars($_POST['id']);
		$mdp = htmlspecialchars($_POST['mdp']);
		
		$connect = $db->prepare("SELECT * FROM accounts WHERE ident=?");
		$connect->execute(array($id));
		$check=$connect->rowCount();
		if($check == 1)
		{
			$data = $connect->fetch(PDO::FETCH_ASSOC);
			
			$dmdp = $data['mdp'];
			if(password_verify($mdp,$dmdp))
			{
				$_SESSION['id'] = $id;
				$_SESSION['mdp'] = $dmdp;
				$setconn = $db->prepare('UPDATE accounts SET conn = ? WHERE mdp = ?');
				$setconn->execute(array(1, $_SESSION['mdp']));
				header('Location: accueil.php');
			}
			else
			{
				$msg = "mauvais mot de passe";
				$_POST['mdp'] = "";
			}
		}
		else
		{
			$msg = "mauvais identifiant";
			$_POST['mdp'] = "";
		}
	}
?>
<html>
	<head>
		<title>Pan Pan Boum Boum</title>
		<link rel="stylesheet" href="style.css"/>
	</head>
	<body>
		<ul>
			<li><a href="accueil.php">Accueil</a></li>
			<li><a href="inscription.php">Inscription</a></li>
			<li><a href="jeu.zip" download>Télécharger</a></li>
		</ul>
		<div class="conn">
			<form method="POST" action="">
				<table width="100%">
					<tr align="center">
						<td>
							<label>Identifiant : </label>
						</td>
						<td>
							<input type="text" name="id" placeholder="votre identifiant"></input>
						</td>
					</tr>
					<tr align="center">
						<td>
							<label>Mot de passe : </label>
						</td>
						<td>
							<input type="password" name="mdp" placeholder="votre mot de passe"></input>
						</td>
					</tr>
					<tr align="center">
						<td colspan="100%">
							</br>
							<p class="error"><?php echo $msg; ?></p>
						</td>
					</tr>
					<tr align="center">
						<td>
							<a class="conn" href="inscription.php">pas encore de compte ?</a>
						</td>
						<td>
							<input type="submit" name="conn" value="connexion"></input>
						</td>
					</tr>
				</table>
			</form>
		</div>
		
		<img class="shooter" name='shooter' src="layinShooterOff.png" width="10%"  onmouseover="shooter.src='layinShooterOn.png'" onmouseout="shooter.src='layinShooterOff'"/>
	</body>
	<footer onmouseover="shooter.src='layinShooterOn.png'" onmouseout="shooter.src='layinShooterOff'">
		<table width="5%">
			<tr align='center'>
				<td><a target="_blank" rel="noopener noreferrer" href="https://github.com/Delectus98/Tactical"><img src="git.png" width='70%'></img></a></td>
				<td><a target="_blank" rel="noopener noreferrer" href="https://github.com/Delectus98/Tactical">Github</a></td>
			</tr>
		</table>
	</footer>
</html>
<?php
	include('conn.php');
?>