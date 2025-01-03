# 넙죽이 픽

---

## Outline

---

<aside>
💡

넙죽이 픽은 넙죽이(제작자)가 카이스트 근처 맛집을 추천해주는 플랫폼입니다!

</aside>

### Team

---

- [박준호](https://www.notion.so/abd6a3ef013e45038b1fafa5f2ca111c?pvs=21)
    - https://github.com/gs18050
- [김유림](https://www.notion.so/b59a13cd1b804ac4858243fb772bbd67?pvs=21)
    - https://github.com/yurimkimm

### Tech Stack

---

**✅  Front-end** : Kotlin

**✅  IDE** : Android studio

**✅  Version Controll** : Github

**✅  External API** : Kakao Maps API

## Details

---

### Splash Screen & Tab Overview

---

<img src="https://github.com/user-attachments/assets/53a6c28e-1c98-44c7-8c3e-7ac4132aae87">

- 앱을 처음 실행하면 앱 로고를 보여주는 
Splash Screen이 표시됩니다.
- `BottomNavigationView` 를 이용해 탭 사이를 
이동할 수 있습니다. 하단에 있는 네비게이션 바에서 
원하는 화면을 누를 경우 해당하는 탭으로 이동합니다.
- 사용자가 탭을 전환할 때 보다 부드럽게 화면이 
이동하여 보기 좋도록 `NavOptions.Builder()`를 사용하여 탭 전환이 보다 자연스럽도록 하였습니다.

### Tab1 : 대전 맛집 리스트 & 추천

---

대전 지역의 맛집 정보들을 보여주는 탭

<img src="https://github.com/user-attachments/assets/c71c36d7-da63-4131-92aa-5ca24ead6203">

- 각 맛집마다 이미지, 가게 이름, 주소를 표시하며, 
전화 걸기 버튼이 제공됩니다. 전화 걸기 버튼을 
누를 경우 `Intent.*ACTION_DIAL`* 을 이용하여 
전화앱 다이얼 화면으로 넘어갑니다.
- 맛집 데이터는 `JSON` 파일에 저장되어 있으며, 
이를 불러와 `RecyclerView`를 통해 화면에 
표시됩니다.
- 특정 맛집의 이미지를 선택할 경우 Tab3에 있는 
맛집 지도에서 위치를 보여줍니다.

<img src="https://github.com/user-attachments/assets/caa822b0-d25d-462c-9830-617767f46c3b">

- 상단에 있는 검색창을 사용해 맛집을 검색할 수 있습니다.
- 오늘의 넙죽이 픽 버튼을 누를 경우 전체 맛집 리스트에서 랜덤으로 넙죽이가 점심/저녁 식당을 추천해줍니다.
- 랜덤으로 추천된 맛집 팝업에서도 이미지를 클릭하여 위치를 보거나 전화를 걸 수 있습니다.

### Tab2 : 대전 맛집 음식 사진첩

---

음식 사진을 위주로 보고 맛집을 고를 수 있는 탭

<img src="https://github.com/user-attachments/assets/dd68175c-a085-4844-be34-ddc68cdf642a">

- 맛집들의 음식 사진들을 보며 메뉴를 고를 수 있습니다.
- 각 이미지를 누를 경우 해당하는 맛집의 위치를 Tab3의 지도에서 보여줍니다.

### Tab3 : 대전 맛집 지도

---

지도를 통해 맛집들의 위치를 볼 수 있는 탭

<img src="https://github.com/user-attachments/assets/1c630795-86eb-4f87-8338-b0630f04ef3c">

<img src="https://github.com/user-attachments/assets/61a047a9-6d11-4a98-9580-f0e2ad6b2943">

- 카카오맵 API를 사용하여 지도를 보여줍니다.
- 처음 탭에 들어갈 경우 현재 위치를 지도에서 보여주며 맛집들의 핑이 지도에 찍혀있습니다.
- 핑을 클릭할 경우 해당 맛집에 대한 상세 정보 및 전화 버튼이 화면에 나옵니다.
- 상단의 검색바를 이용하면 지도에서 맛집들의 위치를 검색할 수도 있습니다.
- 현재 어떤 음식점을 보고 있을 경우 해당 맛집의 핑은 구분이 가도록 다르게 생겼습니다.

### 후기

---

<aside>
🍀

박준호

- Kotlin은 처음 사용해보는데 즐거운 경험이었습니다! 남은 기간이 기대되는 첫 주였던 것 같습니다.
- **오른쪽에 있는 디자이너**께서 디자인을 너무 잘해요!!
- p.s. 다시는 카카오맵 API를 사용하지 않겠습니다…
</aside>

<aside>
🐙

김유림

- 처음 만들어보는 앱이었는데 재미있었고 유익한 경험이었습니다.
- AndroidStudio, Figma를 사용할 줄 알게되어 뿌듯합니다.
- 댕처레전드 짝꿍 준호야 고맙다☺️
</aside>

<img src="https://github.com/user-attachments/assets/e4e0380f-718e-414b-ae14-b044286627ab">

### 넙죽이 픽 apk

---

[NupjuksPick.apk](https://drive.google.com/file/d/1EJoq9dFIk60f31uSMhKvb5W8zckEOFPF/view?usp=sharing)
