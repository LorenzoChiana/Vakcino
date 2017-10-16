<?php
	$servername = "localhost";
	$username = "vakcinoapp";
	$password = "movfabirru44";
	$dbname = "my_vakcinoapp";

	// Create connection
	//$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	/*if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	}

	$result = mysqli_prepare($conn, "SELECT * FROM VACCINAZIONE");    
	mysqli_stmt_execute($result);
	mysqli_stmt_store_result($result);*/

	/*print_r(json_encode($result));
	mysqli_stmt_bind_result($result, $antigene, $name, $description, $group);*/
	/*$a = array();
	$b = array();
	while (mysqli_stmt_fetch($result)) {
	    $b["Antigene"] = $antigene;
		$b["Nome"] = $name;
		$b["Descrizione"] = $description;
		$b["Gruppo"] = $group;
		array_push($a,$b);
	}
	print_r(json_encode($a));
	mysqli_stmt_close($result); 
	mysqli_free_result($result);
	mysqli_close($conn);*/

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);
	// Check connection
	if ($conn->connect_error) {
	    die("Connection failed: " . $conn->connect_error);
	}

	$sql = "SELECT * FROM TIPO_VACCINAZIONE";
	$result = $conn->query($sql);

	$a = array();
	$b = array();
	$i = 0;
	if ($result->num_rows > 0) {
	    // output data of each row
	    while($row = $result->fetch_assoc()) {
	        //echo "id: " . $row["id"]. " - Name: " . $row["firstname"]. " " . $row["lastname"]. "<br>";
	        /*echo $i++ . " ";
	        echo $row["Antigene"] . "<br>";
	        echo $row["Nome"] . "<br>";
	        echo $row["Descrizione"] . "<br>";
	        echo $row["GRUPPO"] . "<br>";*/
			$b["ID"] = $row["ID"];
			$b["Da"] = $row["Da"];
			$b["A"] = $row["A"];
	        $b["TipoImmunizzazione"] = $row["TipoImmunizzazione"];
	        $b["NumRichiamo"] = $row["NumRichiamo"];
	        $b["Antigene"] = $row["Antigene"];
	        $b["Status"] = $row["Status"];
			array_push($a,$b);

	    }
	} else {
	    echo "0 results";
	}
	print_r(json_encode(utf8ize($a)));
	$conn->close();

	function utf8ize($d) {
    if (is_array($d)) {
        foreach ($d as $k => $v) {
            $d[$k] = utf8ize($v);
        }
    } else if (is_string ($d)) {
        return utf8_encode($d);
    }
    return $d;
}

?>