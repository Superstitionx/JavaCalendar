# Java SE Project (Calendar)

## ○ 화면구성

### ○ 메인화면
<img src ="https://postfiles.pstatic.net/MjAxOTA1MTVfMTEw/MDAxNTU3OTExNTA4MTkx.bWT9ChAXMpGqWHuycUHhYgx6FrNAU3PXR35zJDGDUtsg.6Qt8csePC4Z_mFkb43GJwh8q2NK6SKcpLFx2w0g8J-og.PNG.jsh2583/%EB%A9%94%EC%9D%B8%ED%99%94%EB%A9%B4.png?type=w773"/>

### ○ 일정 화면
<img src ="https://postfiles.pstatic.net/MjAxOTA1MTVfOTkg/MDAxNTU3OTExNTM5NjMz.nJ3td-qaCJwhJgTzG8Wp2A16DTAXAJLdoIn7PpIBDfAg.sItu0Kr9Bm9a-R_3fD9swvwkjlB_9RwxkGGjI-oCux8g.PNG.jsh2583/%EC%9D%BC%EC%A0%95%ED%99%94%EB%A9%B4.png?type=w773"/>

### ○ 일정 추가 화면
<img src = "https://postfiles.pstatic.net/MjAxOTA1MTVfMjMg/MDAxNTU3OTExNTYwNjMx.uZbuUZzDnEHztd-6YZNHfb-npWeaW74x8MdTo8s_bBMg.jnt0N-9lXqXtLkL_n_I3KQcV8Ol0ICE6ksG8Fu7QKAgg.PNG.jsh2583/%EC%9D%BC%EC%A0%95_%EC%B6%94%EA%B0%80%ED%99%94%EB%A9%B4.png?type=w773"/>

### ○ 일정 수정 화면
<img src = "https://postfiles.pstatic.net/MjAxOTA1MTVfNDQg/MDAxNTU3OTExNTgwMTAx.ZmXyN9nxrRAK67CggehSdtTzmoml82xbzpEBMC5Xhb4g.SE7SsuKd9-V2WTvgO5jWDSOfYRczbzgw0H0hoRmEVIMg.PNG.jsh2583/%EC%9D%BC%EC%A0%95_%EC%88%98%EC%A0%95_%ED%99%94%EB%A9%B4.png?type=w773"/>

### ○ 디자인 
> Java Swing AWT의 Component를 이용한 Frame 구현 </br>
> LayoutManager를 이용한 구성 요소 배치 </br>

### ○ 로직 구현
> EventListener를 이용한 Event 처리 구현</br>
> Thread를 이용한 시계 구현</br>
> Calendar 내장 함수를 이용한 달력 구현</br>

## ○ 주요 기능
### ○ 달력 기능
<img src="https://postfiles.pstatic.net/MjAxOTA1MTVfMzAg/MDAxNTU3OTE0NDIyMDkx.d14w7mJI14ODqT63O73Ii6tciplRMtjdXFPSjR7ZA9cg.eDMwdtOTPg0Pnk6d-SPTOdYToJP7F5wjdxinQL4Xsiog.PNG.jsh2583/%EB%8B%AC%EB%A0%A5%EA%B8%B0%EB%8A%A51.png?type=w773"/>
<img src ="https://postfiles.pstatic.net/MjAxOTA1MTVfNzEg/MDAxNTU3OTE0NDY1Mzcz.SXNsn6RxR2aVnSbTOT0VnaPRQF1d91Z0xM1BE-0jYVgg.GyPYFrAVkS5uU3WdFx1VgcKxe7URRaSbgg-GmnQvFhog.PNG.jsh2583/%EB%8B%AC%EB%A0%A5%EA%B8%B0%EB%8A%A5.png?type=w773"/>
> MouseWheelListener를 통한 달력 전월 후월 이동 구현</br>
> Button에 Event를 추가하여 Database 연동하는 로직 구현</br>

### ○ 일정 추가 기능
<img src="https://postfiles.pstatic.net/MjAxOTA1MTVfMjIg/MDAxNTU3OTE0NTA3NDAy.jTUjT-sJoDCRIqRhnUdVzEcgquoFjOM7fkS6arJnAuUg.sUxJN0HUVNHle1cl2CQ34ltbnOhMsQIf46IgU3se_A0g.PNG.jsh2583/%EC%9D%BC%EC%A0%95%EC%B6%94%EA%B0%80%EA%B8%B0%EB%8A%A51.png?type=w773"/>
<img src="https://postfiles.pstatic.net/MjAxOTA1MTVfMTY3/MDAxNTU3OTE0NTI2MzM3.xfLLIFHdsCG1oMADyHEmtC0IsZFSkyJ0BxtBgvHHJncg.4pOg0KrJchyFRf8wiLlgT85zg1O97wjiylNIlRaLW3Mg.PNG.jsh2583/%EC%9D%BC%EC%A0%95%EC%B6%94%EA%B0%80%EA%B8%B0%EB%8A%A52.png?type=w773"/>
> Button에 Event를 추가하여 Database와 연동해 일정을 추가하는 로직 구현 </br>
> 추가한 일정을 달력에 추가하는 로직 구현 </br>

### ○ 일정 수정 기능
<img src="https://postfiles.pstatic.net/MjAxOTA1MTVfMTcy/MDAxNTU3OTE0NTQ3Mzcw.tUZfSSXp4bATh3Sar1_Sc6RTmdnaTiGmzS5SqpRmABUg.4VpS9_hicCGk0kYNPePtSTeuZ0LWOaRa95W6RE5lsJkg.PNG.jsh2583/%EC%9D%BC%EC%A0%95_%EC%88%98%EC%A0%95%EA%B8%B0%EB%8A%A5.png?type=w773"/>
> Button에 Event를 추가하여 Database와 연동해 일정을 수정하는 로직 구현 </br>
