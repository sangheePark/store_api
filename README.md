# store_api
Store Management System API
AWS 서버 설정
- Create Instance 
- 기본 설정
sudo su
yum update -y
rm /etc/localtime
ln -s /usr/share/zoneinfo/Asia/Seoul /etc/localtime
amazon-linux-extras enable java-openjdk11 
yum clean metadata 
yum install -y java-11-openjdk

- jenkins(https://wikidocs.net/16280  참조)
wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
yum install jenkins

기본 포트가 8080 포트이기 때문에 포트를 변경해주어야 합니다.(8080포트를 사용하는 다른 서비스가 있다면...)
설정 파일은 /etc/sysconfig/jenkins 경로에 있습니다.
파일 중간에 보면 JENKINS_PORT=“8080” 라는 부분이 있는데 변경할 포트를 입력합니다.
다른 서비스가 사용하지 않는 번호로 지정해 주세요. netstat -nlp 명령어를 입력하면 사용되고 있는 포트를 알 수 있습니다.
포트를 원하시는 포트로 변경하고 저장합니다.

-- AWS 인바운드에 해당 포트 추가
systemctl enable jenkins 
systemctl start jenkins

http://아이피:포트 접속

vi /var/lib/jenkins/secrets/initialAdminPassword
암호입력
