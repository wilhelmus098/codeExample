<?php
include '../conn.php';
include '../checksession.php';

if(isset($_POST['btn_register']))
{
    $id = $_POST['id'];
    $uname = $_POST['username'];
    $pass = $_POST['password'];
    $pos = $_POST['jabatan'];

    addUser($uname, $pass, $pos);

}

if(isset($_POST['btnUpdate']))
{
    $uname = $_POST['uname'];
    $passlama = $_POST['password'];
    $passbaru = $_POST['password1'];
    $passbaru2 = $_POST['password2'];

    cekpassword($passlama, $uname, $passbaru, $passbaru2);
}

if(isset($_POST['btn_login']))
{
    header("Location:../logout.php");
}

// ---------------------------------------------------------
// METHOD
// ---------------------------------------------------------
function addUser($uname,$pwd,$pos)
{
    global $mysqli;
    $sql = "INSERT INTO tUser VALUE(null, '" . $uname . "','" . $pwd . "','" . $pos . "')";
    if (mysqli_query($mysqli, $sql)) 
    {
        echo "<script type='text/javascript'>alert('register succeed');</script>";
       header('Location:../register.php');
    }
    else
    {
       echo "Error: " . $sql . "<br>" . mysqli_error($mysqli);
    }

    mysqli_close($mysqli);
}

function cekpassword($password, $nama, $passbaru, $passbaru2){
    global $mysqli;
    $sql = "SELECT * FROM tUser WHERE user_uname = '" . $nama . "'";
    $result = mysqli_query($mysqli, $sql);
    $passlama = "";

    while($row = $result->fetch_assoc()){
        $passlama = $row['user_pword'];
    }
    // echo "$passbaru<br>";
    // echo "$passbaru2<br>";

    if($password != $passlama){
        echo "err1";
    }
    else if($passbaru != $passbaru2)
    {
        echo "err2";
    }
    else{
        editUser($nama, $passbaru, $passbaru2);
    }
    }

function editUser($uname, $passbaru, $passbaru2)
{
    global $mysqli;
    $sql = "UPDATE tUser SET user_pword = '" . $passbaru . "' WHERE user_uname = '" . $uname . "' ";
    if (mysqli_query($mysqli, $sql))
    {
        echo "ok";
    }
    else
    {
        echo "Error: " . $sql . "<br>" . mysqli_error($mysqli);
    }
}


?>