import SearchIcon from '@mui/icons-material/Search';
import Link from "next/link";

export default function Header() {
    const navMenu = [
        ["소개", "/about"],
        ["Q&A", "/qna"],
    ];

    return (
        <header className={"bg-white h-16"}>
            <div className={"toolbar_contents flex items-center justify-between w-11/12 h-full ml-auto mr-auto"}>
                <p className={"text-3xl font-bold"}>
                    <Link href={"/"}>게시판</Link>
                </p>
                <nav>
                    <ul className={"header-nav_menu"}>
                        {navMenu.map(([name, link], index) => (
                            <li key={index} className={"header-nav_item content-center"}>
                                <Link href={link}
                                      className={"text-base rounded-lg hover:bg-gray-400 hover:bg-opacity-10"}>
                                    {name}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </nav>
                <div className={"header-login_menu flex items-center"}>
                    <div role={"button"} className={"text-sm p-2 relative"}>
                        <SearchIcon/>
                    </div>
                    {/* TODO:: 로그인 페이지로 이동 구현 */}
                    <button>로그인</button>
                    <span>|</span>
                    {/* TODO:: 회원가입 페이지로 이동 구현 */}
                    <button>회원가입</button>
                </div>
            </div>
        </header>
    );
}
