<?php

require_once('conn.php');
global $mysqli;

$sql1 = "SELECT * FROM parking_lot";
$result1 = mysqli_query($mysqli,$sql1);

$jsonarr = array();

if($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        array_push($jsonarr,array(
            "building_id"=>$row1['lot_id'],
            "building_name"=>$row1['lot_name'],
        ));
    }
    echo json_encode(array('parkinglots'=>$jsonarr));
    $mysqli -> close();
}

?>