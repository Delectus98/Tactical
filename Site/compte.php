<?php
	session_start();
	$msg = "";
	
	if(!empty($_SESSION['mdp']))
	{
		include('menuc.php');
		$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
		$get = $db->prepare('SELECT * FROM accounts WHERE mdp = ?');
		$get->execute(array($_SESSION['mdp']));
		$data = $get->fetch(PDO::FETCH_ASSOC);
		
		if(isset($_POST['modname']))
		{
			$newId=$_SESSION['id'];
			$newPwd=$_SESSION['mdp'];
			$pwd=htmlspecialchars($_POST['pwd']);
			
			if(!empty($_POST['newId']))
			{
				$check = $db->prepare('SELECT * FROM accounts WHERE ident = ?');
				$check->execute(array(htmlspecialchars($_POST['newId'])));
				$ok = $check->rowCount();
				if($ok == 0)
				{
					$newId=htmlspecialchars($_POST['newId']);
				}
				else
				{
					$msg = "cet identifiant est déjà utilisé";
				}			
			}
			
			if(!empty($_POST['newPwd']))
			{
				if($_POST['newPwd']==$_POST['valPwd'])
				{
					$newPwd=password_hash(htmlspecialchars($_POST['newPwd']), PASSWORD_ARGON2I);
				}
				else
				{
					$msg="vos deux nouveaux mots de passe ne correspndent pas";
				}
			}
			
			if(password_verify($pwd, $_SESSION['mdp']))
			{
				$modname = $db->prepare('UPDATE accounts SET ident = ?, mdp = ? WHERE mdp = ?');
				$modname->execute(array($newId, $newPwd, $_SESSION['mdp']));
				$_SESSION['id'] = $newId;
				$_SESSION['mdp'] = $newPwd;
				$msg="les données de votre compte ont bien été modifiées";
				header( "Refresh:2; url=compte.php", true, 303);
			}
			else
			{
				$msg="le mot de passe que vous avez entré n'est pas le bon";
			}
			
		}
		
		$countCheck=$db->prepare('SELECT ID FROM messages WHERE dest = ?');
		$countCheck->execute(array($_SESSION['id']));
		$msgCount = $countCheck->rowCount();
		if($msgCount!=0)
		{
			if(isset($_POST['arch']))
			{
				foreach($_POST['archeck'] as $archeck)
				{
					$archmsg=$db->prepare('UPDATE messages SET arch=? WHERE ID=?');
					$archmsg->execute(array(1, $archeck));
				}
			}
			if(isset($_POST['supp']))
			{
				foreach($_POST['archeck'] as $supcheck)
				{
					$supmsg=$db->prepare('DELETE FROM messages WHERE ID=?');
					$supmsg->execute(array($supcheck));
				}
			}
			if(isset($_POST['suparch']))
			{
				foreach($_POST['suparcheck'] as $suparcheck)
				{
					$suparchmsg=$db->prepare('DELETE FROM messages WHERE ID=?');
					$suparchmsg->execute(array($suparcheck));
				}
			}
		}
		$getmsg = $db->prepare('SELECT * FROM messages WHERE dest = ? AND arch="0"');
		$getmsg->execute(array($_SESSION['id']));
		$getamsg = $db->prepare('SELECT * FROM messages WHERE dest = ? AND arch="1"');
		$getamsg->execute(array($_SESSION['id']));
?>
		<html>
			<body>
				<div class="main">
					<table width="80%" align="center" id="acc">
						<tr>
							<td>
								<img class="class" src="imgs/<?php echo $data['ident']; ?>.png"/>
							</td>
							<td>
								Pseudonyme :
							</td>
							<td align="center">
								<?php echo $data['ident']; ?>
							</td>
						</tr>
						<tr>
							<td class="msg" colspan="100%" align="center">
								</br></br>
								<?php echo $msg; ?>
							</td>
						</tr>
						<tr>
							<td colspan="100%" align="center">
								<input type="button" id="mod" value="modifier"></input>
							</td>
						</tr>
					</table>
					<form method='POST' action=''>
						<table width="80%" align="center" id="modAcc" class="hidden">
							<tr>
								<td>
									<img class="class" src="imgs/<?php echo $data['ident']; ?>.png"/>
								</td>
								<td>
									Changer de pseudonyme :
								</td>
								<td align="center">
									<input type="text" name="newId" placeholder="votre nouvel identifiant"/>
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>
									Changer de mot de passe :
								</td>
								<td align="center">
									<input type="password" name="newPwd" placeholder="votre nouveau mot de passe"/>
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>
								</td>
								<td align="center">
									<input type="password" name="valPwd" placeholder="confirmez votre nouveau mot de passe"/>
								</td>
							</tr>
							<tr>
								<td>
								</td>
								<td>
									Votre mot de passe actuel
								</td>
								<td align="center">
									<input type="password" name="pwd" placeholder="mot de passe actuel"/>
								</td>
							</tr>
							<tr>
								<td colspan="100%" align="center">
									</br></br>
									<input type="submit" name="modname" value="valider">   <input type="button" id="ann" value="annuler"></input></input>
								</td>
							</tr>
						</table>
					</form>
					</br>
					
					
					<form method="POST" action="">
						<input type="button" id="toggleMessages" value="Messages (<?php $count=$getmsg->rowCount(); echo $count; ?>)"/>
						</br></br>
						<table align="center" width="100%" id="messagesTab" class="hidden">
							<?php
								while($mdata = $getmsg->fetch(PDO::FETCH_ASSOC))
								{
							?>
									<tr align="center">
										<td>
											from <?php echo $mdata['exp'];?> :
										</td>
										<td align="left">
											<?php echo $mdata['msg'];?>
										</td>
										<td>
											<input type="checkbox" name="archeck[]" value="<?php echo $mdata['ID']?>"/>
										</td>
									</tr>
							<?php
								}
							?>
							<tr align="center">
								<td>
									</br></br>
									<input type="submit" name="arch" value="archiver"/>
								</td>
								<td>
									</br></br>
									<input type="submit" name="supp" value="supprimer"/>
								</td>
							</tr>
						</table>
					</form>
					
					
					<form method="POST" action="">
						<input type="button" id="toggleArchived" value="Messages archivés (<?php $acount=$getamsg->rowCount(); echo $acount; ?>)"/>
						</br></br>
						<table align="center" width="100%" id="archivedTab" class="hidden">
							<?php
								while($amdata = $getamsg->fetch(PDO::FETCH_ASSOC))
								{
							?>
									<tr align="center">
										<td>
											from <?php echo $amdata['exp'];?> :
										</td>
										<td align="left">
											<?php echo $amdata['msg'];?>
										</td>
										<td>
											<input type="checkbox" name="suparcheck[]" value="<?php echo $amdata['ID']?>"/>
										</td>
									</tr>
							<?php
								}
							?>
							<tr align="center">
								<td colspan="100%">
									</br></br>
									<input type="submit" name="suparch" value="supprimer"/>
								</td>
							</tr>
						</table>
					</form>
				</div>
			</body>
		</html>
<?php
	}
	else
	{
		header('Location: accueil.php');
	}
?>

<script type="text/javascript">
	var show = document.getElementById("mod");
	var can = document.getElementById("ann");
	
	show.addEventListener("click", function()
	{
		document.getElementById("acc").style.display = "none"; 
		document.getElementById("modAcc").style.display = "table"; 
	});
	can.addEventListener("click", function()
	{
		document.getElementById("acc").style.display = "table"; 
		document.getElementById("modAcc").style.display = "none"; 
	});
	
	var msg = document.getElementById("toggleMessages");
	var arch = document.getElementById("toggleArchived");
	
	
	msg.addEventListener("click", function()
	{
	  var x = document.getElementById("messagesTab");
	  if (x.style.display === "none")
	  {
		x.style.display = "table";
	  }
	  else
	  {
		x.style.display = "none";
	  }
	});
	
	arch.addEventListener("click", function()
	{
	  var y = document.getElementById("archivedTab");
	  if (y.style.display === "none")
	  {
		y.style.display = "table";
	  }
	  else
	  {
		y.style.display = "none";
	  }
	});
</script>