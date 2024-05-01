'use client';

import './login.css';
import Logo from "@/app/ui/commons/logo";
import Link from "next/link";
import {useEffect, useRef, useState} from 'react';
import axios from "axios";

export default function LoginPage() {
    const emailInputRef = useRef(null);
    const passwordInputRef = useRef(null);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    // 에러 메시지
    const [emailError, setEmailError] = useState('');
    const [passwordError, setPasswordError] = useState('');

    useEffect(() => {
        const handleFocus = () => {
            if (emailError && passwordError) {
                emailInputRef.current.focus();
            } else if (emailError) {
                emailInputRef.current.focus();
            } else if (passwordError) {
                passwordInputRef.current.focus();
            }
        };

        const handleInputStyles = () => {
            if (emailError) {
                emailInputRef.current.classList.add('border-red-600');
            } else {
                emailInputRef.current.classList.remove('border-red-600');
            }

            if (passwordError) {
                passwordInputRef.current.classList.add('border-red-600');
            } else {
                passwordInputRef.current.classList.remove('border-red-600');
            }
        };

        handleFocus();
        handleInputStyles();
    }, [emailError, passwordError]);

    // 로그인 버튼 클릭 시 submit 이벤트 발생
    const handleSubmit = (e) => {
        e.preventDefault();

        // 에러 메시지 초기화
        setEmailError('');
        setPasswordError('');

        if (!email && !password) {
            setEmailError('이메일 주소를 입력해주세요.');
            setPasswordError('비밀번호를 입력해주세요.');
            return;
        } else if (!email) {
            setEmailError('이메일 주소를 입력해주세요.');
            return;
        } else if (!password) {
            setPasswordError('비밀번호를 입력해주세요.');
            return;
        }

        axios.post('/api/auth/token', {
            email: email,
            password: password
        }).then((res) => {
            console.log(res);
        }).catch((err) => {
            console.error(err);
        });
    }

    return (
        <div className='app login-page'>
            <div
                className='flex flex-col w-4/5 lg:w-3/5 xl:w-2/5 2xl:w-4/12 h-full m-auto justify-center align-middle'>
                <main className='w-3/5 m-auto'>
                    <form className='mt-52' action='' onSubmit={handleSubmit}>
                        <Logo/>
                        <div className='flex flex-col'>
                            <label htmlFor='email'>이메일 주소</label>
                            <input
                                ref={emailInputRef}
                                className='h-11 p-2 border-2 focus:outline-none focus:border-blue-600 focus:ring-blue-600 focus:ring-1 rounded border-solid border-inherit'
                                type='email' id='email' name='email'
                                placeholder='이메일 주소를 입력해주세요.'
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />
                            <div className='input-error email'>
                                <span>{emailError}</span>
                            </div>
                        </div>
                        <div className='flex flex-col mt-2'>
                            <label htmlFor='password'>비밀번호</label>
                            <input
                                ref={passwordInputRef}
                                className='h-11 p-2 border-2 focus:outline-none focus:border-blue-600 focus:ring-blue-600 focus:ring-1 rounded border-solid border-inherit'
                                type='password' id='password' name='password'
                                placeholder='비밀번호를 입력해주세요.'
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <div className='input-error password'>
                                <span>{passwordError}</span>
                            </div>
                        </div>
                        <div className='flex flex-col mt-6'>
                            <button type='submit'
                                    className='flex h-11 justify-center bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 items-center'>
                                <span className='material-icons-outlined pr-2.5'></span>
                                <span>로그인</span>
                            </button>
                        </div>
                        <div className='signup-area'>
                            <p className='signup-hint'>아직 회원이 아니신가요? </p>
                            <Link href='/signup' className='signup-button'>가입하기</Link>
                        </div>
                    </form>
                </main>
            </div>
        </div>
    );
}
