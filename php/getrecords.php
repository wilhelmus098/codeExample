<?php

require_once('conn.php');
global $mysqli;

$phone = $_POST["phone"];
//$phone = '098';

//$sql1 = "SELECT * FROM record WHERE record_user='" . $phone . "'";
$sql1 = "SELECT * FROM record WHERE record_user='" . $phone . "' AND record_dateout IS NOT NULL ORDER BY record.record_datein DESC";
$result1 = mysqli_query($mysqli,$sql1);

$jsonarr = array();

if($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        array_push($jsonarr,array(
            "record_id"=>$row1['record_id'],
            "record_user"=>$row1['record_user'],
            "record_slotno"=>$row1['record_slotno'],
            "record_buildingid"=>$row1['record_lotid'],
            "record_datein"=>$row1['record_datein'],
            "record_dateout"=>$row1['record_dateout'],
            "record_cost"=>$row1['record_id']
        ));
    }

    echo json_encode(array('records'=>$jsonarr));
    $mysqli -> close();
}

?>