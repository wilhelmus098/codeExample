<?php

require_once('conn.php');
global $mysqli;

$sql = "SELECT * FROM parking_lot";
$result = mysqli_query($mysqli,$sql);

$jsonarr = array();

if($result->num_rows > 0)
{
    while($row1 = $result->fetch_assoc())
    {
        array_push($jsonarr,array(
            "lot_id"=>$row1['lot_id'],
            "lot_name"=>$row1['lot_name']
        ));
    }

    echo json_encode(array('parkinglot'=>$jsonarr));
    $mysqli -> close();
}
?>