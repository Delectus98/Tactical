<?php
	$db = new PDO('mysql:host=localhost; dbname=project', 'root', '');
	$conn = $db->prepare("SELECT * FROM accounts WHERE conn=1");
	$conn->execute();
	if(isset($_SESSION['mdp']))
	{
		$_SESSION['dest']="";
		if(isset($_POST['mess']))
		{
			$_SESSION['dest']=htmlspecialchars($_POST['mess']);
			header('Location:msg.php');
		}
	}
?>

<div class="class">
	<h3>Utilisateurs connectés</h3>
	<form method="POST" action="">
		<table class="class" width="100%">
			<?php
				while($cdata = $conn->fetch(PDO::FETCH_ASSOC))
				{
			?>
					<tr align='center'>
					<td width="15%">
						<img class='class'src="imgs/<?php echo $cdata['ident']; ?>.png" width="100%"/>
						</td>
						<td align="center">
							<input type="submit" name="mess" value="<?php echo $cdata['ident']; ?>"/>
						</td>
					</tr>
			<?php	
				}
				if(isset($_SESSION['mdp']))
				{
			?>
					<tr align=center>
						<td colspan="100%">
							</br></br>
							<a href="msg.php">Envoyer un message à un membre</a>
						</td>
					</tr>
			<?php
				}
			?>
		</table>
	</form>
</div>