import "./styles.scss";
import * as $ from "jquery";
import Alpine from "alpinejs";
import morph from "@alpinejs/morph";

Alpine.plugin(morph);
// Alpine warns if started before body exists
$(() => Alpine.start());

export * as StompJs from "@stomp/stompjs";
export { $ };
export { Alpine };
