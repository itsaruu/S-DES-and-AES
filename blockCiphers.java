import java.util.Scanner;
import static java.lang.System.out;

class sDES
{
    String s0[][]={ {"01","00","11","10"},
                    {"11","10","01","00"},
                    {"00","10","01","11"},
                    {"11","01","11","10"}};

    String s1[][]={ {"00","01","10","11"},
                    {"10","00","01","11"},
                    {"11","00","01","00"},
                    {"10","01","00","11"}};

    String subKey1,subKey2;

    String IP(String str) {
        return String.valueOf(new char[]{str.charAt(1), str.charAt(5), str.charAt(2), str.charAt(0), str.charAt(3), str.charAt(7), str.charAt(4), str.charAt(6)});
    }
    
    String IP_inv(String str) {
        return String.valueOf(new char[]{str.charAt(3), str.charAt(0), str.charAt(2), str.charAt(4), str.charAt(6), str.charAt(1), str.charAt(7), str.charAt(5)});
    }
    
    String P_10(String str) {
        return String.valueOf(new char[]{str.charAt(2), str.charAt(4), str.charAt(1), str.charAt(6), str.charAt(3), str.charAt(9), str.charAt(0), str.charAt(8), str.charAt(7), str.charAt(5)});
    }
    
    String P_8(String str) {
        return String.valueOf(new char[]{str.charAt(5), str.charAt(2), str.charAt(6), str.charAt(3), str.charAt(7), str.charAt(4), str.charAt(9), str.charAt(8)});
    }
    
    String E_P(String R) {
        return String.valueOf(new char[]{R.charAt(3), R.charAt(0), R.charAt(1), R.charAt(2), R.charAt(1), R.charAt(2), R.charAt(3), R.charAt(0)});
    }
    
    String P_4(String str) {
        return String.valueOf(new char[]{str.charAt(1), str.charAt(3), str.charAt(2), str.charAt(0)});
    }
    
    String XOR(String str1, String str2, int bit) {

        // Convert binary strings to integers
        int num1 = Integer.parseInt(str1, 2);
        int num2 = Integer.parseInt(str2, 2);

        // Perform XOR operation
        int result = num1 ^ num2;

        // Convert result back to binary string
        String output = Integer.toBinaryString(result);
        
        while(output.length()!=bit)
        {
            output = '0' + output;
        }
        return output;
    }
    
    String LCS(String str, int num) {
        String stringOut;
        while (num > 0) {
            stringOut = "";
            char temp = str.charAt(0);
            for (int i = 0; i < 4; i++) {
                stringOut += str.charAt(i+1);
            }
            stringOut += temp;
            num--;
            str = stringOut;
        }
        return str;
    }
    
    void keyExpansion(String key)
    {
        out.println("\nKey Expansion");
        key = P_10(key);
        out.println("Key after P10: " + key);
        
        String Left = key.substring(0, 5);
        String Right = key.substring(5, 10);
        Left = LCS(Left, 1);
        Right = LCS(Right, 1);
        out.println("Key after Left Circular shift by 1 bit: " + Left + " " + Right);
        subKey1 = P_8(Left + Right);
        out.println("SubKey 1 after P8: " + subKey1);
        
        Left = LCS(Left, 2);
        Right = LCS(Right, 2);
        out.println("Key after Left Circular shift by 2 bit: " + Left + " " + Right);
        subKey2 = P_8(Left + Right);
        out.println("SubKey 2 after P8: " + subKey2);
    }
    
    String fiestal(String L, String R, String subKey)
    {
        String rightBit;
        rightBit = E_P(R);
        out.println("After Expansion and Permutation: " + rightBit);
        String output = XOR(rightBit, subKey, 8);
        out.println("After XOR: " + output);
        String sbox0, sbox1;
        String temp;
        temp = "" + output.charAt(0) + output.charAt(3);
        int row = Integer.parseInt(temp, 2);
        out.print("S-box 0: row = " + temp);
        temp = "" + output.charAt(1) + output.charAt(2);
        out.println(", col = " + temp);
        int col = Integer.parseInt(temp, 2);
        sbox0 = s0[row][col];
        temp = "" + output.charAt(4) + output.charAt(7);
        out.print("S-box 1: row = " + temp);
        row = Integer.parseInt(temp, 2);
        temp = "" + output.charAt(5) + output.charAt(6);
        out.println(", col = " + temp);
        col = Integer.parseInt(temp, 2);
        sbox1 = s1[row][col];
        output = sbox0 + sbox1;
        output = P_4(output);
        out.println("P4 permutation:" + output);
        output = XOR(output, L, 4);
        return output;
    }

    void encrypt(String plaintext)
    {
        out.println("\nEncryption\n");
        out.println("Plaintext: " + plaintext);
        plaintext = IP(plaintext);
        out.println("Plain text after Initial Permutation: " + plaintext);
        String Left, Right;
        Left = plaintext.substring(0, 4);
        Right = plaintext.substring(4, 8);
        out.println("Left register: " + Left + " and Right register: " + Right);

        String f1 = fiestal(Left, Right, subKey1);
        out.println("Round 1 output: " + f1);
        Left = Right;
        Right = f1;
        out.println("After swap, Left register:  " + Left + " and Right register: " + Right);

        f1 = fiestal(Left, Right, subKey2);
        out.println("Round 2 output: " + f1);
        String cipherText = f1 + Right;
        out.println("Final data in register: " + cipherText);
        cipherText = IP_inv(cipherText);
        out.println("Inverse Initial Permutation: " + cipherText);
        decrypt(cipherText);
    }


    void decrypt(String cipherText)
    {
        out.println("\nDecryption\n");
        out.println("Cipher Text: " + cipherText);
        cipherText = IP(cipherText);
        out.println("Plain text after Initial Permutation: " + cipherText);
        String Left, Right;
        Left = cipherText.substring(0, 4);
        Right = cipherText.substring(4, 8);
        out.println("Left register: " + Left + " and Right register: " + Right);

        String f1 = fiestal(Left, Right, subKey2);
        out.println("Round 1 output: " + f1);
        Left = Right;
        Right = f1;
        out.println("After swap, Left register:  " + Left + " and Right register: " + Right);

        f1 = fiestal(Left, Right, subKey1);
        out.println("Round 2 output: " + f1);
        String text = f1 + Right;
        out.println("Final data in register: " + text);
        text = IP_inv(text);
        out.println("PlainText: "+text);
    }
}

class AES
{
    final int RCon[] = {0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80,0x1B,0x36};
    final int s[][] =
    {
        {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},
        {0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},
        {0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},
        {0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},
        {0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},
        {0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},
        {0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},
        {0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},
        {0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},
        {0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},
        {0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},
        {0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},
        {0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},
        {0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},
        {0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},
        {0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}
    };
    final int inv_s[][] =
    {
        {0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB},
        {0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB},
        {0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E},
        {0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25},
        {0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92},
        {0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84},
        {0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06},
        {0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B},
        {0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73},
        {0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E},
        {0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B},
        {0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4},
        {0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F},
        {0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF},
        {0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61},
        {0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D}
    };
    
    int key[][] = new int[44][4];
    int gf2n_multiply(int a, int b) 
    {
        final int overflow = 0x100, modul = 0x11B;
        int sum = 0;
        
        while (b > 0) {
            int res = b&1;
            if (res!=0) sum = sum ^ a;     
            b = b >> 1;                    
            a = a << 1;
            int red = a & overflow;                 
            if (red!=0) a = a ^ modul;
        }
        return sum;
    }
    void subBytes(int mat[][])
    {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                int row = mat[i][j] / 16;
                int col = mat[i][j] % 16;
                mat[i][j] = s[row][col];
            }
        }
    }
    void subBytes( int mat[])
    {
        for(int i=0; i<4; i++)
        {
            int row = mat[i] / 16;
            int col = mat[i] % 16;
            mat[i] = s[row][col];
        }
    }
    void InvsubBytes(int mat[][] )
    {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                int row = mat[i][j] / 16;
                int col = mat[i][j] % 16;
                mat[i][j] = inv_s[row][col];
            }
        }
    }

    void LCS( int mat[], int numByte)
    {
        while(numByte>0)
        {
            int temp = mat[0];
            for(int i=0; i<3; i++)
            {
                mat[i]=mat[i+1];
            }
            mat[3] = temp;
            numByte--;
        }
    }
    void RCS( int mat[], int numByte)
    {
        while(numByte>0)
        {
            int temp = mat[3];
            for(int i=3; i>0; i--)
            {
                mat[i]=mat[i-1];
            }
            mat[0] = temp;
            numByte--;
        }
    }

    int[] gFunc(int w[], int i)
    {
        int arr[] = new int[4];
        for(int j=0; j<4; j++) arr[j]=w[j];

        LCS(arr,1);

        subBytes(arr);

        arr[0] = arr[0]^RCon[i-1];
        out.print("\ng(w"+(4*i - 1)+") = ");
        for(int j=0; j<4; j++) out.printf("%02X ", arr[j]);
        out.print("\n");
        return arr;
    }
    void KeyGeneration(int ch[][])
    {
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
                key[i][j] = ch[i][j];
        }
        for(int k=0; k<4; k++)
        {
                out.print("W "+k+" = ");
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", key[k][j]); 
                }
                out.print("\n");
        }
        for(int i=1;i<=10; i++)
        {
            int temp[] = new int[4];
            temp = gFunc(key[(i*4)-1],i);

            for(int j=0; j<4; j++)
                key[(i*4)][j] = key[(i*4)-4][j]^temp[j];

            for(int j=0; j<4; j++)
                key[(i*4)+1][j] = key[(i*4)-3][j]^key[(i*4)][j];

            for(int j=0; j<4; j++)
                key[(i*4)+2][j] = key[(i*4)-2][j]^key[(i*4)+1][j];

            for(int j=0; j<4; j++)
                key[(i*4)+3][j] = key[(i*4)-1][j]^key[(i*4)+2][j];  
            
            for(int k=0; k<4; k++)
            {
                int x = 4 * i + k;
                out.print("W " + x +" = ");
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", key[4*i+k][j]); 
                }
                out.print("\n");
            }
        }
    }
    void AddRoundKey( int mat[][], int k)
    {
        
		
		for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                mat[j][i] = mat[j][i]^key[4*k+i][j];
            }
        }
    }
    void ShiftRow(int mat[][])
    {
        for(int i=0; i<4; i++)
        {
            LCS(mat[i],i);
        }
    }
    void InvShiftRow(int mat[][])
    {
        for(int i=0; i<4; i++)
        {
            RCS(mat[i],i);
        }
    }
    void MixColumns(int mat[][], int temp[][])
    {
        int col[][] = { {0x02,0x03,0x01,0x01},
                        {0x01,0x02,0x03,0x01},
                        {0x01,0x01,0x02,0x03},
                        {0x03,0x01,0x01,0x02}};

        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                int sum=0;
                for(int k=0; k<4; k++)
                {
                    sum = sum ^ (gf2n_multiply(col[i][k],mat[k][j]));  
                }
                temp[i][j] = sum;
            }
        }
    }

    void InvMixColumns(int mat[][], int temp[][])
    {
        int col[][] = { {0x0E,0x0B,0x0D,0x09},
                        {0x09,0x0E,0x0B,0x0D},
                        {0x0D,0x09,0x0E,0x0B},
                        {0x0B,0x0D,0x09,0x0E}};
        
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                int sum=0;
                for(int k=0; k<4; k++)
                {
                    sum = sum ^ (gf2n_multiply(col[i][k],mat[k][j])); 
                }
                temp[i][j] = sum;
            }
        }
    }

    void Encrypt(int plaintext[][], int pt[][])
    {
        out.print("\n\nIntial Paintext State Matrix: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                pt[k][j]=plaintext[j][k];
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }
        AddRoundKey(pt,0);
        out.print("\nPre Round: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }
        

        for(int i=1; i<=9; i++)
        {
            subBytes(pt);
            out.print("\nRound " + i +"\nAfter SubBytes: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }
            ShiftRow(pt);
            out.print("Shift Rows: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }
            int temp[][] = new int[4][4];
            MixColumns(pt,temp);
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    pt[k][j]=temp[k][j];
                }

            }

            out.print("After Mix Columns: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }
            AddRoundKey(pt,i);
            out.print("After Add Round Key: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }
        }
        subBytes(pt);
        out.print("Round 10\nAfter SubBytes: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }

        ShiftRow(pt);
        out.print("Shift Rows: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }

        AddRoundKey(pt,10);
        out.print("After Add Round Key: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }

        out.print("\nEncrypted text: ");
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ",pt[i][j]);
            }
        }
        out.print("\n");


    }
    
    void Decrypt(int pt[][])
    {
        out.print("\n\nIntial Paintext State Matrix: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }

        AddRoundKey(pt,10);
        out.print("\nPre Round: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }

        int count = 1;
        for(int i=9; i>=1; i--)
        {
            out.print("\nRound "+count);
            InvShiftRow(pt);
            out.print("\nRound Inv Shift Rows: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }


            InvsubBytes(pt);
            out.print("After InvSubBytes: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }

            AddRoundKey(pt,i);
            out.print("After Add Round Key: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }


            int temp[][] = new int[4][4]; 
            InvMixColumns(pt,temp);
            for(int k = 0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    pt[k][j] = temp[k][j];
                }
            }
            out.print("After Inv Mix Columns: \n");
            for(int k=0; k<4; k++)
            {
                for(int j=0; j<4; j++)
                {
                    out.printf("%02X ", pt[k][j]); 
                }
                out.print("\n");
            }
            count++;
    
        }
        out.print("\nRound 10");
        InvShiftRow(pt);
        out.print("\nRound Inv Shift Rows: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }

        InvsubBytes(pt);
        out.print("After InvSubBytes: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }
        
        AddRoundKey(pt,0);
        out.print("After Add Round Key: \n");
        for(int k=0; k<4; k++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ", pt[k][j]); 
            }
            out.print("\n");
        }

        out.print("\nDecrypted text: ");
        for(int i=0; i<4; i++)
        {
            for(int j=0; j<4; j++)
            {
                out.printf("%02X ",pt[j][i]);
            }
        }
    }
}

class blockCiphers
{
    static Scanner scan = new Scanner(System.in);
    
    static char dataType;
    //String plainText;
    public static void main(String[] args) 
    {
        menu();
    }

    public static String hexToBinary(String text, int bit)
    {
        int intText = Integer.parseInt(text, 16);
        text = Integer.toBinaryString(intText);
        while(text.length()<bit)
        {
            text = '0' + text;
        }
        return text;
    }

    public static void menu()
    {
        
        while(true)
        {
            int p, k, dF;

            out.print("\n\nSelect binary or hex data input (B/H): ");
            dataType = scan.next().charAt(0);

            if(dataType == 'b' || dataType == 'B' || dataType == 'h' || dataType == 'H') dF = 1;
            else
            {
                out.println("\nNot a valid data type!\n");
                menu();
            }

            out.print("\n[--------------M E N U--------------]\r\n" + //
                                "1. S-DES     2. AES - 128    3. Exit\n\n");

            out.print("Enter choice from menu: ");
            int n = scan.nextInt();

            if(n==3) break;

            if(n == 1)
            {
                sDES sdes = new sDES();
                out.print("Enter plaintext: ");
                String plainText = scan.next();
                p = 1;

                if(dataType=='H'||dataType=='h')
                {
                    plainText = hexToBinary(plainText, 8);
                }
                else{
                    while(plainText.length()<8) plainText = '0'+plainText;
                }

                boolean validate = validatePlainText(p, plainText);
                if(!validate)
                {
                    out.print("Text not validated!\n\n");
                    menu();
                }

                out.print("Enter Key: ");
                String key = scan.next();
                k = 1;

                if(dataType=='H'||dataType=='h')
                {
                    key = hexToBinary(key, 10);
                }
                else 
                {
                    while(key.length()<10) key = '0'+key;
                }

                boolean validateKey = validateKey(p, key);

                if(!validateKey)
                {
                    out.print("Key not validated!\n\n");
                    menu();
                }

                sdes.keyExpansion(key);
                sdes.encrypt(plainText);
            }

            else if(n == 2)
            {
                AES aes = new AES();
                int pt[][] = new int[4][4];
                out.print("Enter plaintext: ");
                String plainText = scan.next();
                p = 2;
                if(dataType=='H'||dataType=='h')
                {
                    while( plainText.length()<32) plainText = '0' + plainText;
                }
                else{
                    while( plainText.length()<128) plainText = '0' + plainText;
                }
                boolean validate = validatePlainText(p, plainText);
                int plaintext[][] = new int[4][4];
                if(!validate)
                {
                    out.print("Text not validated!\n\n");
                    menu();
                }
                else
                {
                    int q=0;
                    for(int i = 0; i<4; i++)
                    {
                        for(int j = 0; j<4; j++)
                        {
                            String temp = plainText.substring(q,q+2);
                            plaintext[i][j] = Integer.parseInt(temp, 16);
                            q = q + 2;
                        }
                    }
                }

                out.print("Enter Key: ");
                String key = scan.next();
                k = 2;
                if(dataType=='H'||dataType=='h')
                {
                    while( key.length()<32) key = '0' + key;
                }
                else
                {
                    while( key.length()<128) key = '0' + key;
                }
                boolean validateKey = validateKey(p, key);
                int Key[][] = new int[4][4];
                if(!validateKey)
                {
                    out.print("Key not validated!\n\n");
                    menu();
                }
                else
                {
                    int q=0;
                    for(int i = 0; i<4; i++)
                    {
                        for(int j = 0; j<4; j++)
                        {
                            String temp = key.substring(q,q+2);
                            Key[i][j] = Integer.parseInt(temp, 16);
                            q = q + 2;
                        }
                    }
                }

                aes.KeyGeneration(Key);
                aes.Encrypt(plaintext, pt);
                aes.Decrypt(pt);
            }

            else
            {
                out.println("Please enter a valid choice and try again!");
                menu();
            }
        }
    }

    static boolean validatePlainText(int p, String pt)
    {
        if(p == 1)
        {
            if(pt.length()!=8) return false;
            else
            {
                for(char c: pt.toCharArray())
                {
                    if (c != '0' && c != '1') return false;
                }
            }
        }
        else if(p==2)
        {
            if(dataType == 'b'|| dataType == 'B')
            {
                if(pt.length()!=128) return false;
                else
                {
                    for(char c: pt.toCharArray())
                    {
                        if (c != '0' && c != '1') return false;
                    }
                }
            }
            else
            {
                pt = pt.toUpperCase();
                for(int i=0; i<pt.length(); i++)
                {
                    
                    if(Character.isDigit(pt.charAt(i))) continue;
                    else if(pt.charAt(i)=='A'||pt.charAt(i)=='B'||pt.charAt(i)=='C'||pt.charAt(i)=='D'||pt.charAt(i)=='E'||pt.charAt(i)=='F')
                        continue;
                    else return false;
                }
                if(pt.length()!=32) return false;

                return true;
            }
        }
        return true;
    }

    static boolean validateKey(int k, String key)
    {
        if(k == 1)
        {

            if(key.length()!=10) return false;
            else
            {
                for(char c: key.toCharArray())
                {
                    if (c != '0' && c != '1') return false;
                }
            }
        }
        else if(k==2)
        {
            if(dataType == 'b'|| dataType == 'B')
            {
                if(key.length()!=128) return false;
                else
                {
                    for(char c: key.toCharArray())
                    {
                        if (c != '0' && c != '1') return false;
                    }
                }
            }
            else
            {
                key = key.toUpperCase();
                for(int i=0; i<key.length(); i++)
                {
                    
                    if(Character.isDigit(key.charAt(i))) continue;
                    else if(key.charAt(i)=='A'||key.charAt(i)=='B'||key.charAt(i)=='C'||key.charAt(i)=='D'||key.charAt(i)=='E'||key.charAt(i)=='F')
                        continue;
                    else return false;
                }
                if(key.length()!=32) return false;

                return true;
            }
        }
        return true;
    }
}