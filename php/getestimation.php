<?php

require_once('conn.php');
global $mysqli;

$buildingid = $_POST["buildingid"];
$recordid = $_POST["recordid"];

// $buildingid = "5";
// $recordid = "51";
$cost = 0;
$hourlyprice = "";
$dailyprice = "";
$twohours = "";
$datein = new DateTime();
$dateout = new DateTime();

//GET DATEOUT
$sql4 = "SELECT NOW() as curtime";
$result4 = mysqli_query($mysqli, $sql4);
if ($result4->num_rows > 0)
{
    while($row4 = $result4->fetch_assoc())
    {
        $dateout = new DateTime($row4['curtime']);
    }
}
else
{
    echo "error2 : cant get current date time";
    exit(2);
}

//GET DATEIN
$sql5 = "SELECT record.record_datein FROM record WHERE record.record_id ='" . $recordid . "'";
$result5 = mysqli_query($mysqli, $sql5);
if ($result5->num_rows > 0)
{
    while($row5 = $result5->fetch_assoc())
    {
        $datein = new DateTime($row5['record_datein']);
    }
}
else
{
    echo "error3 : cant get date in";
    exit(3);
}

//GET PRICE
$sql6 = "SELECT * FROM parking_lot WHERE parking_lot.lot_id = '" . $buildingid . "'";
$result6 = mysqli_query($mysqli, $sql6);
if ($result6->num_rows > 0)
{
    while($row6 = $result6->fetch_assoc())
    {
        $hourlyprice = $row6['lot_hourly'];
        $dailyprice = $row6['lot_daily'];
        $twohours = $row6['lot_2hours'];
    }
}
else
{
    echo "error5 : cant get price";
    exit(5);
}

//COMPARE DATEIN VS DATEOUT
$interval = $datein->diff($dateout);
$second = $interval->s;
$minute = $interval->i;
$hour = $interval->h;
$day = $interval->d;
//echo $second . " ; " . $hour . " ; " . $day . " ; " . $minute . "<br>";

//CALCULATE PARKING COST
if ($dateout->format('d-m-Y') > $datein->format('d-m-Y'))
{
    if ($day == 0)
    {
        $cost = $dailyprice;
        echo $cost;
    }
    else
    {
        $cost = ($day * $dailyprice) + $dailyprice;
        echo $cost;
        //echo $cost . " = " . $day . "*" . $dailyprice . " + " . $dailyprice;
    }
}
if ($dateout->format('d-m-Y') == $datein->format('d-m-Y'))
{
    //IF FIRST 2 HOURS
    if ($hour < 2)
    {
        if($hour == 0)
        {
            //IF FIRST 5 MINUTES
            if($minute < 5)
            {
                $cost = 0;
                echo $cost;
            }
            else
            {
                $cost = $twohours;
                echo $cost;
            }
        }
        //> 5 MINUTES < 2 HOURS
        else
        {
            $cost = $twohours;
            echo $cost;
            
        }
    }
    else
    {
        $cost = (($hour - 1) * $hourlyprice) + $twohours;
        echo $cost;
    }
}
?>