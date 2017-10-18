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

	$id = $_POST["ID"];
	$first_name = $_POST["Nome"];
	$last_name = $_POST["Cognome"];
	$birthday = $_POST["DataNascita"];
	$type = $_POST["Tipo"];
	$email = $_POST["Email"];
	$status = $_POST["Status"];

	$result = mysqli_prepare($conn, "INSERT INTO UTENTE (ID, nome, cognome, dataNascita, tipo, email, Status) VALUES ((?), (?), (?), (?), (?), (?), (?))");    
	mysqli_stmt_bind_param($result, "isssssi", $id, $first_name, $last_name, $birthday, $type, $email, $status);
	
	if(!mysqli_stmt_execute($result)){
		echo "SONO DENTRO";
		$result = mysqli_prepare($conn, "UPDATE UTENTE SET nome = (?), cognome = (?), dataNascita = (?), tipo = (?), Status = (?) WHERE ID = (?) AND email = (?)");    
		mysqli_stmt_bind_param($result, "ssssiis", $first_name, $last_name, $birthday, $type, $status, $id, $email);
		mysqli_stmt_execute($result);
	} else {
		mysqli_stmt_store_result($result);
		
		print_r(json_encode($result));
	}
	mysqli_stmt_close($result); 
	mysqli_free_result($result);
	mysqli_close($conn);

?>