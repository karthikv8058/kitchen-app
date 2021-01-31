import Locale from "./Locale";

export default interface Restaurant {
    uuid: string;
    ip: string;
    name: string;
    locales: Locale[];
    server_ip: string;
    token:string;
}
