'use client';

import {useState} from "react";
import {useRouter} from "next/navigation";

import CloseIcon from "@mui/icons-material/Close";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Link from "next/link";
import Logo from "@/app/ui/commons/logo";

export default function Header() {
    const router = useRouter();

    const [isNavMenuOpen, setIsNavMenuOpen,
        isLogin] = useState(false);

    const navMenu = [
        ["소개", "/about"],
        ["Q&A", "/qna"],
    ];

    const toggleNavMenu = () => {
        setIsNavMenuOpen(!isNavMenuOpen);
    };

    return (
        <header>
            <div className="container">
                <div className="container_inner">
                    <Logo />
                    <div className={`mobile-nav-menu`}>
                        <div role={"button"} className={"text-sm p-2 relative"}>
                            <SearchIcon className="text-gray-500"/>
                        </div>
                        <div role={"button"}
                             className={"search-icon p-2 relative ml-2"} onClick={() => router.push('/login')}>
                            {isLogin ? <AccountCircleIcon/> : <button>로그인</button>}
                        </div>
                        <div role={"button"} className={"text-sm p-2 relative"}
                             onClick={toggleNavMenu}>
                            {isNavMenuOpen ? <CloseIcon className="text-gray-500"/> :
                                <MenuIcon className="text-gray-500"/>}
                        </div>
                    </div>
                    <div className={`nav_content ${isNavMenuOpen ? 'open' : ''}`}>
                        <div className="separator"></div>
                        <ul className={"nav-menu"}>
                            {
                                navMenu.map((menu, index) => {
                                    return (
                                        <li key={index} className="nav-item">
                                            <Link
                                                href={menu[1]}>{menu[0]}</Link>
                                        </li>
                                    );
                                })
                            }
                            <div className="login-container">
                                <div role={"button"}
                                     className={"search-icon text-sm p-2 relative ml-14"}>
                                    <SearchIcon/>
                                </div>
                                <div role={"button"}
                                     className={"search-icon p-2 relative ml-2"} onClick={() => router.push('/login')}>
                                    {isLogin ? <AccountCircleIcon/> : <button>로그인</button>}
                                </div>
                            </div>
                        </ul>
                    </div>
                </div>
            </div>
        </header>
    );
}
