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


	$result = mysqli_prepare($conn, "UPDATE UTENTE SET Status = 1 WHERE ID = (?)");    
	    mysqli_stmt_bind_param($result, "s", $id);
	    mysqli_stmt_execute($result);
	/*    mysqli_stmt_store_result($result);


	    mysqli_stmt_bind_result($result, $id, $first_name, $last_name, $birthday, $type, $email);
		$a = array();
		$b = array();
	while (mysqli_stmt_fetch($result)) {
	    $b["ID"] = $id;
		$b["Nome"] = $first_name;
		$b["Cognome"] = $last_name;
		$b["DataNascita"] = $birthday;
		$b["Tipo"] = $type;
		$b["Email"] = $email;
		array_push($a,$b);
	}
	$a = array();
	$b = array();
	$b["id"] = $array_id;
	array_push($a,$b);
	print_r(json_encode($a));*/
	mysqli_stmt_close($result); 
	mysqli_free_result($result);
	mysqli_close($conn);

?>