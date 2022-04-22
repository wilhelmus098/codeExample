<?php

require_once('conn.php');
global $mysqli;

$phone = $_POST["phone"];
$lotid = $_POST['buildingid'];
//$phone = '098';

$sql1 = "SELECT * FROM record WHERE record.record_user = '" . $phone . "' AND record.record_dateout IS NULL AND record.record_lotid='" . $lotid . "'";
$result1 = mysqli_query($mysqli,$sql1);

if ($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        echo $row1['record_id'] . ";" . $row1['record_slotno'];
    }
}

$mysqli -> close();
?>