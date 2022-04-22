<?php
include '../conn.php';
include '../checksession.php';

if(isset($_POST['btn_detaillot']))
{
    $id_lot=$_POST['btn_detaillot'];
    header('Location:../detaillot.php?id='.$id_lot);
}

if(isset($_POST['btn_editlot']))
{
    $id_lot=$_POST['btn_editlot'];
    header('Location:../editlot.php?id='.$id_lot);
}

if(isset($_POST['btn_updatelot']))
{

    $id = $_POST['btn_updatelot'];
    $name = $_POST['parkinglot_name'];
    $desc = $_POST['parkinglot_desc'];
    $hourly = $_POST['parkinglot_hourlyprice'];
    $twohours = $_POST['parkinglot_2hours'];
    $daily = $_POST['parkinglot_dailyprice'];
    if($_FILES['parkinglot_mapimg']['error'] == '4')
    {
        echo "no file selected";
    }
    else
    {
        unlink("../img/".$id.".jpg");
        addImage($id,$_FILES);
    }
    editLot($id,$name,$desc,$hourly,$twohours,$daily);
    header('Location:../detaillot.php?id='.$id);
    //print_r($_POST);
}

if (isset($_POST['btn_slotunavailable']))
{

    $arraysize = 0;
    $arraysize1 = 0;
    print_r($_POST);
    if (isset($_POST['slotno']))
    {
        $arraysize = count($_POST['slotno']);
    }
    if (isset($_POST['slotno1']))
    {
        $arraysize1 = count($_POST['slotno1']);
    }
    
    $lotid = $_POST['btn_slotunavailable'];
    for($i = 0; $i<$arraysize; $i++)
    {
        $slotno = $_POST['slotno'][$i];
        setSlotUnavailable($lotid,$slotno);
        //echo $_POST['slotno'][$i] . "<br>";
    }
    for($x = 0; $x<$arraysize1; $x++)
    {
        $slotno1 = $_POST['slotno1'][$x];
        setSlotAvailable($lotid,$slotno1);
    }
    header('Location:../detaillot.php?id='.$lotid);
    //echo $size;
    //setSlotUnavailable("5","101");
}

function setSlotUnavailable($lotid,$slotno)
{
    global $mysqli;
    $sql = "UPDATE slot SET slot_status='unavailable' WHERE slot_no='" . $slotno . "' AND slot_lotid='" . $lotid . "'";
    if (mysqli_query($mysqli,$sql))
    {
        echo "ok";
    }
    else
    {
        echo "Error : " . $sql . "<br>" . mysql_error($mysqli);
    }
}

function setSlotAvailable($lotid,$slotno)
{
    global $mysqli;
    $sql = "UPDATE slot SET slot_status='available' WHERE slot_no='" . $slotno . "' AND slot_lotid='" . $lotid . "'";
    if (mysqli_query($mysqli,$sql))
    {
        echo "ok";
    }
    else
    {
        echo "Error : " . $sql . "<br>" . mysql_error($mysqli);
    }
}

function editLot($id,$name,$desc,$hourly,$twohours,$daily)
{
    global $mysqli;
    $sql = "UPDATE parking_lot SET lot_name ='" . $name . "', lot_desc = '" . $desc . "', lot_hourly = '" . $hourly . "', lot_daily = '" . $daily ."', lot_2hours = '" . $twohours . "' WHERE lot_id ='" . $id . "'";
    if (mysqli_query($mysqli, $sql))
    {
        echo "ok";
        //header('Location:../index.php'); // balik ke home or whatever ganti aja
    }
    else
    {
        echo "Error: " . $sql . "<br>" . mysqli_error($mysqli);
    }
    mysqli_close($mysqli);
}
function addImage($id,$_FILE)
{
    global $mysqli;
    $filename = $id.".jpg";
    $tmpname = $_FILE['parkinglot_mapimg']['tmp_name'];
    $imgdir = "../img/";
    $upload = move_uploaded_file($tmpname, $imgdir.$filename);
    if($upload)
    {
        echo "success upload image";
        // UPDATE PATH
        $sql2 = "UPDATE parking_lot SET lot_path ='" . $filename . "' WHERE lot_id ='" . $id . "'";
        if(mysqli_query($mysqli,$sql2))
        {
            echo "ok update img";
            //header('Location: ../index.php');
        }
        else
        {
            echo "Error: " . $sql2 . "<br>" . mysqli_error($mysqli);
        }
    }
    else
    {
        echo "error img";
    }
}
?>