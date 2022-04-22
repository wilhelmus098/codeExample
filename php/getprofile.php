<?php

require_once('conn.php');
global $mysqli;

$phone = $_POST["phone"];
//$phone = '098';

$sql1 = "SELECT * FROM user WHERE user_phone='" . $phone . "'";
$result1 = mysqli_query($mysqli,$sql1);

$jsonarr = array();

if($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        array_push($jsonarr,array(
            "user_phone"=>$row1["user_phone"],
            "user_name"=>$row1["user_name"],
            "user_email"=>$row1["user_email"],
            "user_balance"=>$row1["user_balance"]
        ));
    }

    echo json_encode(array('profile'=>$jsonarr));
    $mysqli -> close();
}

?>