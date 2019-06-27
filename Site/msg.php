<?php
	session_start();
	include('menuc.php');
	$msg="";
	if(!empty($_SESSION['mdp']))
	{
		$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
		if(isset($_POST['val']))
		{
			if($_POST['dest']!="")
			{
				if(!empty($_POST['msg']))
				{
					$dest=$_POST['dest'];
					$msg=htmlspecialchars($_POST['msg']);
					$send= $db->prepare('INSERT INTO messages(exp, dest, msg) VALUES(?, ?, ?)');
					$send->execute(array($_SESSION['id'], $dest, $msg));
					$msg="votre message a bien été envoyé";
				}
				else
				{
					$msg="veuillez taper votre message";
				}
				
			}
			else
			{
				$msg="veuillez choisir un destinataire";
			}
			
		}
		$select=$db->prepare('SELECT ident FROM accounts WHERE ident !=?');
		$select->execute(array($_SESSION['dest']));
?>
	<div class="main">
		<form method="POST" target="">
			<table align="center">
				<tr align="center">
					<td>
						destinataire :
						</br></br>
					</td>
					<td>
						<select class="styled" name="dest">
							<option value="<?php echo $_SESSION['dest']; ?>"><?php echo $_SESSION['dest']; ?></option>
							<?php
								while($sdata = $select->fetch(PDO::FETCH_ASSOC))
								{
							?>
									<option value="<?php echo $sdata['ident']; ?>"><?php echo $sdata['ident']; ?></option>
							<?php
								}
							?>
						</select>
						</br></br>
					</td>
				</tr>
				<tr align="center">
					<td>
						votre message :
					</td>
					<td>
						<textarea maxlength="75" class="styled" name="msg"></textarea>
					</td>
				</tr>
				<tr align="center">
					<td colspan="100%">
						</br></br>
						<?php echo $msg; ?>
					</td>
				</tr><tr align="center">
					<td colspan="100%">
						</br></br>
						<input type="submit" name="val" value="envoyer"/>
					</td>
				</tr>
			</table>
		</form>
	</div>
<?php
	}
	else
	{
		header('Location: accueil.php');
	}
?>