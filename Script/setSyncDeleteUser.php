<?php
$servername = "localhost";
$username = "vakcinoapp";
$password = "movfabirru44";
$dbname = "my_vakcinoapp";

		// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
		// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
}

$id = $_POST["id"];


$result = mysqli_prepare($conn, "DELETE FROM LIBRETTO WHERE ID = (?)");    
mysqli_stmt_bind_param($result, "s", $id);
mysqli_stmt_execute($result);
$result = mysqli_prepare($conn, "DELETE FROM UTENTE WHERE ID = (?)");    
mysqli_stmt_bind_param($result, "s", $id);
mysqli_stmt_execute($result);
mysqli_stmt_close($result); 
mysqli_free_result($result);
mysqli_close($conn);
?>