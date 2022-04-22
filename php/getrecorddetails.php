<?php

require_once('conn.php');
global $mysqli;

$id = $_POST["recordid"];
//$phone = '098';

$sql1 = "SELECT * FROM record WHERE record.record_id = '" . $id . "'";
$result1 = mysqli_query($mysqli,$sql1);

if ($result1->num_rows > 0)
{
    while($row1 = $result1->fetch_assoc())
    {
        echo $row1['record_id'] . ';' . $row1['record_user'] . ';' . $row1['record_slotno'] . ';' . $row1['record_lotid'] . ';' . $row1['record_datein'] . ';' . $row1['record_dateout'] . ';' . $row1['record_cost'];
    }
}

$mysqli -> close();
?>