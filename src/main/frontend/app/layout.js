import "./globals.css";
import PropTypes from 'prop-types';
import Header from "@/app/header";

export const metadata = {
    title: "게시판",
    description: "JPA 개발 공부용 게시판입니다.",
};

export default function RootLayout({children}) {
    return (
        <html lang="ko">
            <body>
                <Header/>
                {children}
            </body>
        </html>
    );
}

RootLayout.propTypes = {
    children: PropTypes.node.isRequired,
}
