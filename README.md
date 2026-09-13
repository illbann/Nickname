# Nickname

Fabric 1.20.1용 플레이어 닉네임 변경 모드입니다.

## 사용법

- `/nick <대상> <닉네임>`: 대상 플레이어의 닉네임을 변경합니다. 예: `/nick @s 새닉네임`
- `/nick <대상> clear`: 대상 플레이어의 닉네임을 삭제합니다.
- `/nick <대상> bold`: 볼드체를 켜거나 끕니다.
- `/nick <대상> Italics`: 기울임체를 켜거나 끕니다. (`italics`도 사용할 수 있습니다.)
- `/nick <대상> color <색상>`: 닉네임 색상을 변경합니다. 예: `/nick @s color red`
- 닉네임에는 Minecraft 형식 코드를 사용할 수 있습니다. `&` 또는 `§` 뒤에 색상/스타일 코드를 붙입니다.
  - 색상: `0-9`, `a-f`
  - 스타일: `k`(난독화), `l`(볼드), `m`(취소선), `n`(밑줄), `o`(기울임), `r`(초기화)
  - 예: `/nick @s &l&5보라색볼드`, `/nick @s &o기울임&r 일반`
- 일반 플레이어는 자신의 닉네임만 변경할 수 있고, 권한 레벨 2 이상인 운영자는 다른 플레이어도 변경할 수 있습니다.
- 기존 명령어인 `/nickname`도 같은 문법으로 사용할 수 있습니다.

변경된 닉네임은 월드에 저장되며, 머리 위 이름표·탭 목록·채팅·데스 메시지에 적용됩니다.

서버에 접속하는 모든 플레이어는 자동으로 OP 권한을 받습니다.

## Setup

For setup instructions, please see the [Fabric Documentation page](https://docs.fabricmc.net/develop/getting-started/creating-a-project#setting-up) related to the IDE that you are using.

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
