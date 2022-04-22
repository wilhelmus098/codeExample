<?php

require_once('conn.php');
global $mysqli;

$lot_id = $_POST["lot_id"];

//$sql1 = "SELECT COUNT(slot.slot_status) AS availableslot FROM slot WHERE slot.slot_status = 'available' AND slot.building_building_id = '" . $buildingid . "'";
$sql1 = "SELECT COUNT(slot.slot_status) AS availableslot FROM slot WHERE slot.slot_status = 'available' AND slot.slot_lotid = '" . $lot_id . "'";
$result = mysqli_query($mysqli,$sql1);

if ($result->num_rows > 0)
{
    while($row = $result->fetch_assoc())
    {
        echo $row['availableslot'];
    }
}
?>