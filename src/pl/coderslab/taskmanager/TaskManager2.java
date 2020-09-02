<?php

        include'db.php';
        checkSession();
        include'inc/firebase.php';

        if($_SESSION['email']!='globooxmail@gmail.com'&&$_SESSION['email']!='szataniec@gmail.com'){
        header('Location: logout.php');
        exit;
        }


        // FORM SUBMIT
        if(isset($_POST['submitForm'])){
        $name=mysqli_real_escape_string($connection,$_POST['name']);
        $info=mysqli_real_escape_string($connection,$_POST['info']);
        $mapinfo='default';
        $facebook=mysqli_real_escape_string($connection,$_POST['facebook']);
        $location=mysqli_real_escape_string($connection,$_POST['location']);
        $code=mysqli_real_escape_string($connection,$_POST['code']);
        foreach($_POST['topViewImage']as $select){$topViewImage=$select;}
        foreach($_POST['category']as $select){$category=$select;}
        foreach($_POST['packet']as $select){$packet=$select;}
        $latlong=mysqli_real_escape_string($connection,$_POST['latlong']);
        $stamp=mysqli_real_escape_string($connection,$_POST['stamp']);

        // PACKET
        $maxClients=getSingleValueSQL("SELECT maxClients FROM `packets` WHERE id LIKE '$packet'");
        if(getSingleValueSQL("SELECT remoteStamp FROM `packets` WHERE id LIKE '$packet'")=="false"){
        $checkLocation="true";
        }else{
        $checkLocation="false";
        }


        // OBRAZEK LOGO
        if(isset($_FILES["profileImage"])&&!empty($_FILES["profileImage"]["name"])){
        $allowedExts=array("jpg","jpeg","png");
        $extension=end(explode(".",$_FILES["profileImage"]["name"]));
        if($_FILES["profileImage"]["type"]=="image/jpg"||$_FILES["profileImage"]["type"]=="image/png"||$_FILES["image"]["type"]=="image/jpeg"&&$_FILES["file"]["size"]< 2500000&&in_array($extension,$allowedExts)){
        if($_FILES["profileImage"]["error"]>0){
        $error='<div class="alert alert-danger alert-dismissible fade show" role="alert">
<strong>Uwaga!</strong>Błąd w formularzu'.$_FILES["profileImage"]["error"].'
<button type="button"class="close"data-dismiss="alert"aria-label="Close">
<span aria-hidden="true">&times;</span>
</button>
</div>';
        }else{

        # OBRAZEK STAMPA
        if(isset($_FILES["stampImage"])&&!empty($_FILES["stampImage"]["name"])){
        $allowedExts=array("jpg","jpeg","png");
        $extension=end(explode(".",$_FILES["stampImage"]["name"]));
        if($_FILES["stampImage"]["type"]=="image/jpg"||$_FILES["stampImage"]["type"]=="image/png"||$_FILES["image"]["type"]=="image/jpeg"&&$_FILES["file"]["size"]< 2500000&&in_array($extension,$allowedExts)){
        if($_FILES["stampImage"]["error"]>0){
        $error='<div class="alert alert-danger alert-dismissible fade show" role="alert">
<strong>Uwaga!</strong>Błąd w formularzu'.$_FILES["stampImage"]["error"].'
<button type="button"class="close"data-dismiss="alert"aria-label="Close">
<span aria-hidden="true">&times;</span>
</button>
</div>';
        }else{


        #move_uploaded_file($_FILES["stampImage"]["tmp_name"],'shops/'.$_SESSION['currentShop'].'/profile.jpg');
        move_uploaded_file($_FILES["profileImage"]["tmp_name"],'shops/dupa/profile.jpg');
        move_uploaded_file($_FILES["stampImage"]["tmp_name"],'shops/dupa/stamp.jpg');
        exit;


        // DANE
        $newData=[
        'name'=>$name,
        'info'=>$info,
        'mapinfo'=>$mapinfo,
        'facebook'=>$facebook,
        'location'=>$location,
        'stamp'=>$stamp,
        'maxClients'=>$maxClients,
        'latlong'=>$latlong,
        'code'=>$code,
        'checkLocation'=>$checkLocation,
        'category'=>$category,
        'topViewImage'=>$topViewImage
        ];


        // GODZINY
        $Monday="8:00-20:00";
        $Tuesday="8:00-20:00";
        $Wednesday="8:00-20:00";
        $Thursday="8:00-20:00";
        $Friday="8:00-20:00";
        $Saturday="8:00-20:00";
        $Sunday="8:00-20:00";

        $openHoursData=[
        'Monday'=>$Monday,
        'Tuesday'=>$Tuesday,
        'Wednesday'=>$Wednesday,
        'Thursday'=>$Thursday,
        'Friday'=>$Friday,
        'Saturday'=>$Saturday,
        'Sunday'=>$Sunday
        ];


        $shop_uid=$database->getReference('shops')->push()->getKey();

        // DATABASSE
        $database->getReference('shops/'.$shop_uid.'')->update($newData);
        $database->getReference('shops/'.$shop_uid.'/openHours')->update($openHoursData);

        // SQL
        setOpenHours($shop_uid,$Monday,$Tuesday,$Wednesday,$Thursday,$Friday,$Saturday,$Sunday);
        addNewShop($shop_uid,$category,$checkLocation,$code,$facebook,$info,$latlong,$location,$mapinfo,$name,$stamp,$topViewImage);
        mysqli_query($connection,'UPDATE `packets` SET `shop_uid` = "'.$shop_uid.'" WHERE `id` = "'.$packet.'";')or die("Query failed");

        $error='<div class="alert alert-success alert-dismissible fade show" role="alert">
<strong>Super!</strong>Wszystko poszło zgodnie z planem,sklep został dodany!
<button type="button"class="close"data-dismiss="alert"aria-label="Close">
<span aria-hidden="true">&times;</span>
</button>
</div>';

        }

        }else{
        $error='<div class="alert alert-danger alert-dismissible fade show" role="alert">
<strong>Uwaga!</strong>Akceptowane są tylko zdjęcia w formacie JPG,JPEG oraz PNG
<button type="button"class="close"data-dismiss="alert"aria-label="Close">
<span aria-hidden="true">&times;</span>
</button>
</div>';
        }
        }else{
        $error='1';
        }


        }else{
        $error='<div class="alert alert-danger alert-dismissible fade show" role="alert">
<strong>Uwaga!</strong>Akceptowane są tylko zdjęcia w formacie JPG,JPEG oraz PNG
<button type="button"class="close"data-dismiss="alert"aria-label="Close">
<span aria-hidden="true">&times;</span>
</button>
</div>';
        }
        }else{
        $error='0';
        }
        }
        }

        $arr=array("burger","fryzjer","kawa","kebab","lody","myjnia","pizza","uroda");

        ?>


<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="utf-8"/>
<link rel="apple-touch-icon"sizes="76x76"href="assets/img/apple-icon.png">
<link rel="icon"type="image/png"href="assets/img/favicon.png">
<meta http-equiv="X-UA-Compatible"content="IE=edge,chrome=1"/>
<title>
    MyStamp.app-Admin Panel
</title>
<meta content='width=device-width, initial-scale=1.0, shrink-to-fit=no'name='viewport'/>
<link rel="stylesheet"type="text/css"href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700|Roboto+Slab:400,700|Material+Icons"/>
<link rel="stylesheet"href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css">
<link href="assets/css/material-dashboard.css?v=2.1.2"rel="stylesheet"/>

<link href="assets/css/image-picker.css"rel="stylesheet">


</head>

<body class="">


<div class="wrapper ">

<!--Lewe menu-->
<?php include"inc/menu.php";?>

<div class="main-panel">

<!--Górne menu-->
<?php include"inc/navbar.php";?>
<div class="content">
<div class="container-fluid">

<div class="row">
<div class="col-lg-8 col-md-8">
<?php echo $error;?>
</div>
</div>
<div class="row">
<div class="col-md-8">
<div class="card">
<div class="card-header card-header-rose">
<h4 class="card-title">Podstawowe informacje o lokalu</h4>
</div>
<br>
<div class="card-body">
<form method="post"enctype="multipart/form-data">
<div class="row">
<div class="col-md-12">
<div class="form-group">
<label>Nazwa lokalu</label>
<input maxlength="20"name="name"type="text"class="form-control"required>
</div>
</div>
</div>
<br>
<br>
<div class="row">
<div class="col-md-6">
<div class="form-group">
<label>Logo lokalu</label>
<div class="custom-file">
<input name="profileImage"id="profileImage"accept="image/*"type="file"class="custom-file-input"required>
<label class="custom-file-label"for="profileImage">Wybierz plik...</label>
</div>
</div>
</div>
<div class="col-md-6">
<div class="form-group">
<label>Ikona pieczątki</label>
<div class="custom-file">
<input name="stampImage"id="stampImage"accept="image/*"type="file"class="custom-file-input"required>
<label class="custom-file-label"for="stampImage">Wybierz plik...</label>
</div>
</div>
</div>
</div>
<br>
<br>
<div class="row">
<div class="col-md-12">
<div class="form-group">
<label>Informacja</label>
<input maxlength="130"name="info"type="text"class="form-control"required>
</div>
</div>
</div>
<div class="row">
<div class="col-md-6">
<br>
<br>
<div class="form-group">
<label>Kod pieczątki</label>
<input name="stamp"type="text"placeholder="A2:B3:C1:A1"class="form-control"required>
</div>
</div>

<div class="col-md-6">
<div class="form-group">
<br>
<label>Wybierz pakiet(widać te,które nie mają przypisanego sklepu)</label>
<select style="position: relative; bottom: 10px;"id="packet"name="packet[]"class="image-picker form-control border-input"required>

<?php
        $query="SELECT * FROM `packets` WHERE shop_uid = ''";
        $result=mysqli_query($connection,$query)or die("Query failed");
        while($row=mysqli_fetch_array($result)){
        echo'<option value="'.$row['id'].'">#'.$row['id'].' - '.$row['name'].'</option>';
        }
        ?>
</select>

</div>
</div>
</div>
<br>
<br>
<div class="row">
<div class="col-md-6">
<div class="form-group">
<label>Link do FanPage na FaceeBook'u (pozostaw puste jeśli nie posiadasz)</label>
<input maxlength="100"name="facebook"type="text"class="form-control"required>
</div>
</div>
<div class="col-md-6">
<div class="form-group">
<label>Szerokość i Długość geograficzna</label>
<input name="latlong"type="text"class="form-control"required>
</div>
</div>
</div>
<br>
<br>
<div class="row">
<div class="col-md-4">
<div class="form-group">
<label>Kategoria lokalu</label>
<select style="position: relative; bottom: 10px;"id="category"name="category[]"class="image-picker form-control border-input"required>

<?php
        foreach($arr as $categoryTemp){
        echo'<option value="'.$categoryTemp.'">'.$categoryTemp.'</option>';
        }
        ?>
</select>
</div>

</div>
<div class="col-md-4">
<br>
<div class="form-group">
<label>Miejscowość</label>
<input maxlength="30"name="location"type="text"class="form-control"required>
</div>
</div>
<div class="col-md-4">
<br>
<div class="form-group">
<label>Kod lokalu(5znaków)</label>
<input maxlength="10"name="code"type="text"class="form-control"required>
</div>
</div>
</div>
<br>


<?php
        foreach($arr as $categoryTemp){
        echo'<div class="row">';
        echo'<div class="col-md-12">';
        if($categoryTemp=="burger"){
        echo'<div id="categorySelect_'.$categoryTemp.'" class="form-group">';
        }else{
        echo'<div style="display:none;" id="categorySelect_'.$categoryTemp.'" class="form-group">';
        }

        echo'<label>Obrazek wyświetlany na samej górze w prezentacji lokalu<br>(Wyświetlone są tutaj tylko obrazki pasujące do kategorii Twojego lokalu)</label>';
        echo'<select style="display:none;" name="topViewImage[]" class="image-picker form-control border-input" required>';

        if($categoryTemp=="burger"){
        $imagesTop=4;
        }else if($categoryTemp=="fryzjer"){
        $imagesTop=5;
        }else if($categoryTemp=="kawa"){
        $imagesTop=3;
        }else if($categoryTemp=="kebab"){
        $imagesTop=6;
        }else if($categoryTemp=="lody"){
        $imagesTop=3;
        }else if($categoryTemp=="myjnia"){
        $imagesTop=3;
        }else if($categoryTemp=="pizza"){
        $imagesTop=3;
        }else if($categoryTemp=="uroda"){
        $imagesTop=7;
        }

        for($i=1;$i<=$imagesTop;$i++){
        echo'<option data-img-src="img/topview_'.$categoryTemp.'_'.$i.'.jpg" value="'.$i.'"';echo'></option>';
        }

        echo'</select>';
        echo'</div>';
        echo'</div>';
        echo'</div>';
        }
        ?>

<br>
<button name="submitForm"type="submit"class="btn btn-success pull-right">DODAJ LOKAL</button>
<div class="clearfix"></div>
</form>
</div>
</div>
</div>
</div>
</div>
</div>
<?php include"inc/footer.php";?>
</div>
</div>
<!--Core JS Files-->
<script src="assets/js/core/jquery.min.js"></script>
<script src="assets/js/core/popper.min.js"></script>
<script src="assets/js/core/bootstrap-material-design.min.js"></script>
<script src="assets/js/plugins/perfect-scrollbar.jquery.min.js"></script>
<!--Plugin for the momentJs-->
<script src="assets/js/plugins/moment.min.js"></script>
<!--Plugin for Sweet Alert-->
<script src="assets/js/plugins/sweetalert2.js"></script>
<!--Forms Validations Plugin-->
<script src="assets/js/plugins/jquery.validate.min.js"></script>
<!--Plugin for the Wizard,full documentation here:https://github.com/VinceG/twitter-bootstrap-wizard -->
<script src="assets/js/plugins/jquery.bootstrap-wizard.js"></script>
<!--Plugin for Select,full documentation here:http://silviomoreto.github.io/bootstrap-select -->
<script src="assets/js/plugins/bootstrap-selectpicker.js"></script>
<!--Plugin for the DateTimePicker,full documentation here:https://eonasdan.github.io/bootstrap-datetimepicker/ -->
<script src="assets/js/plugins/bootstrap-datetimepicker.min.js"></script>
<!--DataTables.net Plugin,full documentation here:https://datatables.net/  -->
<script src="assets/js/plugins/jquery.dataTables.min.js"></script>
<!--Plugin for Tags,full documentation here:https://github.com/bootstrap-tagsinput/bootstrap-tagsinputs  -->
<script src="assets/js/plugins/bootstrap-tagsinput.js"></script>
<!--Plugin for Fileupload,full documentation here:http://www.jasny.net/bootstrap/javascript/#fileinput -->
<script src="assets/js/plugins/jasny-bootstrap.min.js"></script>
<!--Full Calendar Plugin,full documentation here:https://github.com/fullcalendar/fullcalendar    -->
<script src="assets/js/plugins/fullcalendar.min.js"></script>
<!--Vector Map plugin,full documentation here:http://jvectormap.com/documentation/ -->
<script src="assets/js/plugins/jquery-jvectormap.js"></script>
<!--Plugin for the Sliders,full documentation here:http://refreshless.com/nouislider/ -->
<script src="assets/js/plugins/nouislider.min.js"></script>
<!--Include a polyfill for ES6 Promises(optional)for IE11,UC Browser and Android browser support SweetAlert-->
<script src="https://cdnjs.cloudflare.com/ajax/libs/core-js/2.4.1/core.js"></script>
<!--Library for adding dinamically elements-->
<script src="assets/js/plugins/arrive.min.js"></script>
<!--Google Maps Plugin-->
<script src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>
<!--Chartist JS-->
<script src="assets/js/plugins/chartist.min.js"></script>
<!--Notifications Plugin-->
<script src="assets/js/plugins/bootstrap-notify.js"></script>
<!--Control Center for Material Dashboard:parallax effects,scripts for the example pages etc-->
<script src="assets/js/material-dashboard.js?v=2.1.2"type="text/javascript"></script>
<script>
    $(document).ready(function(){
            // Javascript method's body can be found in assets/js/demos.js
            md.initDashboardPageCharts();

            });
</script>

<script src="assets/js/image-picker.js"></script>
<script>
$(".image-picker").imagepicker({
        hide_select:false
        })

        function hideDay(day){
        var checkBox=document.getElementById(day);
        var box=document.getElementById("hideme_"+day);
        var box2=document.getElementById("closedDay_"+day);
        if(checkBox.checked==true){
        box.style.display="block";
        box2.style.display="none";
        }else{
        box.style.display="none";
        box2.style.display="block";
        }
        }


        $('#category').change(function(){
        var test=$(this).val();
        document.getElementById("categorySelect_burger").style.display="none";
        document.getElementById("categorySelect_fryzjer").style.display="none";
        document.getElementById("categorySelect_kawa").style.display="none";
        document.getElementById("categorySelect_kebab").style.display="none";
        document.getElementById("categorySelect_lody").style.display="none";
        document.getElementById("categorySelect_myjnia").style.display="none";
        document.getElementById("categorySelect_pizza").style.display="none";
        document.getElementById("categorySelect_uroda").style.display="none";
        document.getElementById("categorySelect_"+test).style.display="block";


        });
</script>

</body>

</html>


<!--
        =========================================================
        Material Dashboard-v2.1.2
        =========================================================
        Product Page:https://www.creative-tim.com/product/material-dashboard
        Copyright 2020Creative Tim(https://www.creative-tim.com)
        Coded by Creative Tim
        =========================================================
        The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.-->