var x = document.getElementById("login-form");
var y = document.getElementById("register-form");
var z = document.getElementById("pointer-btn");
var l = document.getElementById("login");
var r = document.getElementById("register");
var ac = document.getElementById("action_title");

function register(){
    x.style.left = "-450px";
    y.style.left = "25px";
    z.style.left = "215px";
    l.style.color = "#848484";
    r.style.color = "#00ffc3";
    ac.textContent = "Register";
}

function login(){
    x.style.left = "25px";
    y.style.left = "450px";
    z.style.left = "30px";
    l.style.color = "#00ffc3";
    r.style.color = "#848484";
    ac.textContent = "Login";
}

document.getElementById("login-form").addEventListener("submit",async function(event){
    event.preventDefault();
    let user={};
    for(const elem of document.querySelectorAll("input[type=text],input[type=password]")){
        user[elem.name]=elem.value;
    }

    console.log(user);
    var formBody = [];
    for (var property in user) {
        var encodedKey = encodeURIComponent(property);
        var encodedValue = encodeURIComponent(user[property]);
        formBody.push(encodedKey + "=" + encodedValue);
    }
    formBody = formBody.join("&");

    console.log(formBody);

    await Userlogin(formBody);

    for(const elem of document.querySelectorAll("input[type=text],input[type=password]")){
        elem.value="";
    }
})

document.getElementById("register-form").addEventListener("submit",async function(event2){
    event2.preventDefault();
    //form validation using JS.
    let str = document.forms["registerCheck"]["password"].value;
    if (str == null || str.length <8) {
       alert("password need more than 8 characters!")
    }else{
        let reg1 = new RegExp(/^[0-9A-Za-z]+$/);
        if (!reg1.test(str)) {
            alert("Password can only contain letters and numbers!")
        }else{
            let reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
            if (reg.test(str)) {
                let userRegister={};
                for(const elem of document.querySelectorAll("input[type=text],input[type=password]")){
                    userRegister[elem.name]=elem.value;
                }
                await UserRegister(userRegister)
                for(const elem of document.querySelectorAll("input[type=text],input[type=password]")){
                    elem.value="";
                }
            }else{
                alert("Password must contain at least 1 number and 1 letter!")
            }
        }
    }

})




async function Userlogin(formBody){
    try{
        const response = await fetch('http://localhost:8080/kitchen/login',{
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formBody
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="404"){
                    alert("please register!");
                }else if(data.toString()==="200"){
                    self.location="dish.html"
                }
            })
    }catch (e){
        console.error(e);
        alert("Something went wrong!")
    }

}

async function UserRegister(user){
    try{
        const response = await fetch('http://localhost:8080/kitchen',{
            method: "POST",
            headers: {
                'Content-Type': 'Application/json'
            },
            body: JSON.stringify(user)
        }).then(res=>res.json())
            .then(data=>{
                if(data.toString()==="405"){
                    alert("405 Register error! please try again.");
                }else if(data.toString()==="201"){
                    alert("You have successfully registered!");
                }
            })
    }catch (e){
        console.error(e);
        alert("Something went wrong!")
    }

}
