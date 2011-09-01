<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Spring Twitter</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"/>
</head>
<body>
<form>
    <fieldset>
        <legend>Hello new user!</legend>
        <p>
            <label for="j_username">What's your name?:</label>
            <br/>
            <input type="text" name="j_username" id="j_username"/>
            <span id="checkname"></span>
        </p>

        <p>
            <label for="j_password">Password:</label>
            <br/>
            <input type="password" name="j_password" id="j_password"/>
        </p>

        <p>
            <button id="register" type="button">Register me!</button>
        </p>
    </fieldset>
</form>
<script type="text/javascript">
    $(document).ready(function() {
        $("#register").click(function() {
                    var usern = $("#j_username").val();
                    var passw = $("#j_password").val();
                    $.post("/register", { username:usern, password:passw },
                        function(data) {
                            alert("Data Loaded: " + data);
                        }
                    );
        });
        $("#j_username").blur(function() {
            var usern = $("#j_username").val();
            $.get("/exists?name=" + usern,
                function(data) {
                    if (data) {
                        $("#checkname").text("User already exists with that name");
                    } else {
                        $("#checkname").text("Name is free to use.");
                    }
                }
            );
        });
    })
    ;
</script>
</body>
</html>