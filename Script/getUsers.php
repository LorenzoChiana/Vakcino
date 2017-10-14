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

	$email = $_POST["email"];


	$result = mysqli_prepare($conn, "SELECT * FROM UTENTE WHERE email = (?)");    
	    mysqli_stmt_bind_param($result, "s", $email);
	    mysqli_stmt_execute($result);
	    mysqli_stmt_store_result($result);


	    mysqli_stmt_bind_result($result, $id, $first_name, $last_name, $birthday, $type, $email, $status);
		$a = array();
		$b = array();
	while (mysqli_stmt_fetch($result)) {
	    $b["ID"] = $id;
		$b["Nome"] = $first_name;
		$b["Cognome"] = $last_name;
		$b["DataNascita"] = $birthday;
		$b["Tipo"] = $type;
		$b["Email"] = $email;
		$b["Status"] = $status;
		array_push($a,$b);
	}
	print_r(json_encode($a));
	mysqli_stmt_close($result); 
	mysqli_free_result($result);
	mysqli_close($conn);

?>