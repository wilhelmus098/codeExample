<?php

require_once('conn.php');
global $mysqli;

$phone = $_POST["phone"];
//$phone = '098';

$sql1 = "SELECT * FROM record, parking_lot WHERE record_user='" . $phone . "' && record_dateout IS NULL && record_lotid = parking_lot.lot_id";
$result1 = mysqli_query($mysqli,$sql1);

$jsonarr = array();

if($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        array_push($jsonarr,array(
            "record_user"=>$row1["record_user"],
            "record_datein"=>$row1["record_datein"],
            "record_slotno"=>$row1["record_slotno"],
            "lot_name"=>$row1["lot_name"],
            "lot_path"=>$row1["lot_path"],
            "lot_desc"=>$row1["lot_desc"]
        ));
    }

    echo json_encode(array('currentActivity'=>$jsonarr));
    $mysqli -> close();
}

?>