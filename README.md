# bakupProj
Help you backup the packer automatically and regularly


#1.linux shell</br>
#2.java process</br>
#3.zip or tar.gz</br>
#4.send email or phone</br>
#5.Springboot ---@Scheduled</br>
#6.mvn package jar</br>
#7.java -jar bakupProj.jar</br>



基于springboot的自动化备份,主打java版的自动化备份</br>
linux的cron固然方便，但是还是不及java定时器的灵活</br>
你可以去连数据库，做成接口交互，发邮件发短信，扩展性非常好，且代码量非常少</br>
在使用java虚机执行shell的时候遇到了很多坑，不过还好核心问题解决了</br>
使用这个项目，你需要修改配置的路径，以及想要备份的文件的路径,至于代码里面就是CTRL+C,V</br>



##使用说明
这是一个java项目,使用java执行shell脚本，进行打包zip的操作</br>
定时器执行打包，定时执行检查打包情况，并把打包的成功失败情况以邮件形式通知</br>
    <code>
    @Scheduled(cron = "0 0 6 * * ? ")
    public void bakUp() {
    }
    </code>
打成jar包之后， 直接启动 <h5>java -jar bakupProj.jar </h5>
后台启动<h5>java -jar bakupProj.jar &</h5>

#环境需求:
jdk1.8</br>
456端口需要开启</br>
支持shell脚本</br>

