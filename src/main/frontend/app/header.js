'use client';

import {useState} from "react";

import CloseIcon from "@mui/icons-material/Close";
import MenuIcon from "@mui/icons-material/Menu";
import SearchIcon from "@mui/icons-material/Search";
import Link from "next/link";

export default function Header() {
  const [isNavMenuOpen, setIsNavMenuOpen] = useState(false);

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
            <div className="header-logo">
              <Link href="/">BOARD</Link>
            </div>
            <div className={`mobile-nav-menu`}>
              <div role={"button"} className={"text-sm p-2 relative"}>
                <SearchIcon className="text-gray-500"/>
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
                        <li className="nav-item">
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
                  <button>로그인</button>
                  <button>회원가입</button>
                </div>
              </ul>
            </div>
          </div>
        </div>
      </header>
  );
}
