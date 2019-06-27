<?php
	session_start();
	include('menu.php');
	$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
	
	$msg="";

	if(isset(($_POST['val'])))
	{
		$mdp=htmlspecialchars($_POST['mdp']);
		$valmdp=htmlspecialchars($_POST['valmdp']);
		$id=htmlspecialchars($_POST['id']);
		
		if(!empty($id)&&!empty($mdp)&&!empty($valmdp))
		{
			if($mdp==$valmdp)
			{
				$check= $db->prepare("SELECT * FROM accounts WHERE ident=?");
				$check->execute(array($id));
				$ok=$check->rowCount();
				if($ok==0)
				{
					$mdp=password_hash($mdp, PASSWORD_ARGON2I);

					$insert = $db->prepare("INSERT INTO accounts(ident, mdp, conn) VALUES(?, ?, ?)");
					$insert->execute(array($id, $mdp, 1));
					$_SESSION['id'] = $id;
					$_SESSION['mdp'] = $mdp;
					header('Location: accueil.php');
				}
				else
				{
					$msg="ce pseudonyme est déjà utilisé";
				}
			}
			else
			{
				$msg="les deux mots de passe ne correspondent pas";
			}
		}
		else
		{
			$msg="Veuillez remplir tous les champs";
		}
	}
?>
<html>
	<div class='main'>
		<form method='POST' action="" enctype="multipart/form-data">
			<table align='center' >
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
					<td>
						<label></label>
					</td>
					<td>
						<input type="password" name="valmdp" placeholder="retapez votre mot de passe"></input>
					</td>
				</tr>
				<tr align="center">
					<td colspan="100%">
						</br>
						<p class="error"><?php echo $msg; ?></p>
					</td>
				</tr>
				<tr align="center">
					<td colspan="100%">
						<input type="submit" name="val" value="m'inscrire"></input>
					</td>
				</tr>
			</table>
		</form>
	</div>
</html>