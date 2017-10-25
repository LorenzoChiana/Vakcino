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

	$id_user = $_POST["IDUtente"];
	$email = $_POST["Email"];
	$id_vacType = $_POST["IDTipoVac"];
	$done = $_POST["Fatto"];
	$date = $_POST["InData"];
	$status = $_POST["Status"];

	if(strcmp($date, "") !== 0){
		$result = mysqli_prepare($conn, "INSERT INTO LIBRETTO (ID, email, ID_VAC, fatto, in_data, Status) VALUES ((?), (?), (?), (?), (?), (?))"); 
	}
	else {
		$date = null;
		$result = mysqli_prepare($conn, "INSERT INTO LIBRETTO (ID, email, ID_VAC, fatto, in_data, Status) VALUES ((?), (?), (?), (?), (?), (?))");

	}  
	mysqli_stmt_bind_param($result, "isiisi", $id_user, $email, $id_vacType, $done, $date, $status);
	
	if(!mysqli_stmt_execute($result)){
		echo "SONO DENTRO";
		$result = mysqli_prepare($conn, "UPDATE LIBRETTO SET Status = (?) WHERE ID = (?) AND email = (?) AND ID_VAC = (?)");    
		mysqli_stmt_bind_param($result, "iisi", $status, $id_user, $email, $id_vacType);
		mysqli_stmt_execute($result);
	} else {
		mysqli_stmt_store_result($result);
		
		print_r(json_encode($result));
	}
	mysqli_stmt_close($result); 
	mysqli_free_result($result);
	mysqli_close($conn);

?>