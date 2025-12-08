import React from "react";
import {useNavigate, useParams} from "react-router-dom";
import AppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";

export default function TopBar() {
    const navigate = useNavigate();
    const { bookId } = useParams();
    const token = localStorage.getItem("accessToken");

    /* ---------------------- 글 삭제 ---------------------- */
    const handleDelete = async () => {
        if (!window.confirm("정말 삭제하시겠습니까?")) return;

        try {
            const response = await fetch(`/api/v1/books/${bookId}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${token}`,  // JWT 사용 시
                },
            });

            // 403: 작성자가 아님
            if (response.status === 403) {
                const error = await response.json();
                alert(error.message);
                return;
            }

            // 기타 실패
            if (!response.ok) {
                alert("글 삭제 중 오류가 발생했습니다.");
                return;
            }

            // 204 성공 → 목록 페이지로 이동
            alert("삭제가 완료되었습니다.");
            navigate("/books");

        } catch (err) {
            console.error(err);
            alert("서버와 통신 중 오류가 발생했습니다.");
        }
    };

    return (
        <AppBar position="static">
            <Toolbar sx={{ position: "relative" }}>
                {/* 왼쪽: 뒤로가기 버튼 */}
                <IconButton
                    edge="start"
                    color="inherit"
                    onClick={() => navigate('/books')}
                >
                    <ArrowBackIcon />
                </IconButton>

                {/* 중앙: 제목 */}
                <Typography
                    variant="h6"
                    sx={{
                        position: "absolute",
                        left: "50%",
                        transform: "translateX(-50%)",
                        fontWeight: "bold",
                        pointerEvents: "none",   // 버튼 클릭 방해하지 않도록
                    }}
                >
                    상세 조회
                </Typography>

                {/* 오른쪽: 글수정 버튼 + 글삭제 버튼 */}
                <Button
                    color="inherit"
                    sx={{ ml: "auto",mr:1 }}
                    onClick={() => navigate(`/books/${bookId}/edit`)}
                >
                    글수정
                </Button>
                <Button
                    color="inherit"
                    onClick={handleDelete}
                >
                    글삭제
                </Button>
            </Toolbar>
        </AppBar>
    );
}