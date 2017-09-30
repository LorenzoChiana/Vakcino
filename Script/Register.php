<?php
    $connect = mysqli_connect("localhost", "vakcinoapp", "movfabirru44", "my_vakcinoapp");
    
    $email = $_POST["email"];
    $password = $_POST["password"];
    
    function registerUser() {
        global $connect, $email, $password;
        $statement = mysqli_prepare($connect, "INSERT INTO ACCOUNT(email, pw) VALUES (?, ?)");
        
        mysqli_stmt_bind_param($statement, "ss", $email, $password);
        if(mysqli_stmt_execute($statement)){
        	mysqli_stmt_close($statement); 
            return true;
        } else {
        	mysqli_stmt_close($statement); 
            return false;
        }
    }
    function usernameAvailable() {
        global $connect, $username;
        $statement = mysqli_prepare($connect, "SELECT * FROM ACCOUNT WHERE email = ?"); 
        mysqli_stmt_bind_param($statement, "s", $email);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }
    
    $response = array();
    $response["success"] = false;  
    if (usernameAvailable()){
        if(registerUser()) {
        	$response["success"] = true;  
        }
    }
    
    echo json_encode($response);
?>