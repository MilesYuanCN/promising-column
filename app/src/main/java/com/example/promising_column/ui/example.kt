package com.example.promising_column.ui

/**
 * TODO 简要描述
 * @author upshermiles @ Zebra Inc.
 * @since 03-04-2022
 */

val exampleJson =
    """
{
    "blocks": [
        {
            "contents": [
                {
                    "text": "非常"
                },
                {
                    "ids": [
                        0,
                        1
                    ],
                    "text": "高兴"
                },
                {
                    "ids": [
                    1
                    ],
                    "text": "今天能够在这里和大"
                },
                {
                "ids": [
                1,
                    3
                    ],
                    "text": "家"
                },
                {
                    "ids": [
                    ],
                    "text": "分享一下"
                },
                {
                    "ids": [
                        2
                    ],
                    "text": "语文文本"
                },
                {
                    "ids": [
                        2
                    ],
                    "text": "编辑器"
                },
                {
                    "text": "的实现方式"
                }
            ]
        }
    ],
    "entityMap": {
        "0": {
            "controlType": "HIGHLIGHT"
        },
        "1": {
            "controlType": "ITALIC"
        },
        "2": {
            "controlType": "NATUARL_SPELLING",
            "data": {
                "commenttext": "这是一个批注文本",
                "commentImageUrl": "https://批注图示.jpg"
            }
        },
        "3" : {
            "controlType": "SPACE"
        }
    }
}
"""