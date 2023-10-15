# PTIS(Public Transportation Information System)
대중교통 길찾기를 위해 만든 프로젝트

# 요구사항
1. application.properties 필요
```
이 안에 API Endpoint 및 API Key등 모든 상수가 보관되어 있음
프로젝트 자체는 껍데기로서 API를 받아주는 기능만 함
```
2. 공공데이터 API 계정 신청 필요
```
application.properties에 넣을 공공 API KEY가 필요
https://www.data.go.kr/ 들어가서 신청하면 됨

신청하고 주말에는 허가를 안내주고 평일에는 수 시간 내로 허가 내주는 듯
```

# 특징
* 서울시 버스 정류장정보 API 지원
* 서울시 버스 노선정보 API 지원 (개발중)
* 국토교통부 버스 노선정보 API 지원 (개발중)
* 공공 API를 이용한 길찾기 API 지원 (개발중)

# 잡담
```
이름은 그냥 프로젝트 명 필요해서 그냥 막 넣음
사실 따지고 보면 교통정보가 아니라 길찾기를 위한 시스템이라
Route Finding 뭐 이런 이름으로 지어야하겠지만... 지금 생각해보니까 쫌 별로네

백엔드와 프로젝트를 나눈 이유는 XML이 들어가다 보니까 생각보다 볼륨이 크고
추후 길찾기 시스템이 가져야 할 확장성을 고려할 때 나누는게 맞다고 판단 되었기 때문임
```